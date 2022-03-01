package com.solenov.adapterweather;

import com.solenov.adapterweather.model.MessageA;
import com.solenov.adapterweather.model.MessageB;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.MediaType;

@SpringBootApplication
public class AdapterWeatherApplication extends RouteBuilder {

    @Value("${server.port}")
    String serverPort;

    @Value("${serviceA.api.path}")
    String contextPath;

    @Autowired
    private WeatherService weatherAddingService;

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean servlet = new ServletRegistrationBean
                (new CamelHttpTransportServlet(),
                        contextPath + "/*");
        servlet.setName("CamelServlet");
        return servlet;
    }

    public static void main(String[] args) {
        SpringApplication.run(AdapterWeatherApplication.class, args);
    }

    @Override
    public void configure() throws Exception {
        configureRestService();

        rest("/api/").description("Test REST ServiceA")
                .id("routeA")
                .post("/add-weather")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .bindingMode(RestBindingMode.auto)
                .type(MessageA.class)
                .enableCORS(true)
                .to("direct:remoteServiceA");

        from("direct:remoteServiceA")
                .routeId("routeB")
                .tracing()
                .process(exchange -> {
                    MessageA messageA = (MessageA) exchange.getIn().getBody();
                    MessageB messageB = weatherAddingService.addWeatherDataToMessage(messageA);
                    exchange.getIn().setBody(messageB);
                })
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201));
    }

    private void configureRestService() {
        restConfiguration().contextPath(contextPath)
                .port(serverPort)
                .enableCORS(true)
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Test REST API")
                .apiProperty("api.version", "v1")
                .apiProperty("cors", "true")
                .apiContextRouteId("doc-api")
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

    }
}

