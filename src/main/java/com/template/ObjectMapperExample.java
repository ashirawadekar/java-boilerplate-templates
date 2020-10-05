package com.template;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.model.Address;
import com.template.model.Car;
import com.template.model.Gender;
import com.template.model.Person;
import com.template.resttemplate.ObjectMapperSupplier;

/**
 * Two good articles
 * https://www.baeldung.com/jackson-object-mapper-tutorial
 * https://www.baeldung.com/jackson-annotations
 */
public class ObjectMapperExample {

    public static void main(String args[]) throws IOException {

        ObjectMapper objectMapper = ObjectMapperSupplier.getObjectMapper();

        // Uncomment to test
//        writeJsonFromObject(objectMapper);

        // Uncomment to test
//        Person person = objectMapper.readValue(fetchResource("person_with_unknown_data.json"), Person.class);
//        System.out.println(person);

        String json = "{ \"color\" : \"Black\", \"type\" : \"FIAT\" }";
        JsonNode jsonNode = objectMapper.readTree(json);
        String color = jsonNode.get("color").asText();
        System.out.println(color);

        String jsonCarArray = "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
        List<Car> listCar = objectMapper.readValue(jsonCarArray, new TypeReference<List<Car>>(){});
        System.out.println(listCar);

        Map<String, Object> map
                = objectMapper.readValue(json, new TypeReference<Map<String,Object>>(){});
        System.out.println("MAP -> " + map);
        Car caaar = new Car();
        caaar.setColor("Black");
        caaar.setId(1);
        caaar.setType("Kia");
        System.out.println(objectMapper.writeValueAsString(caaar));

    }

    private static void writeJsonFromObject(ObjectMapper objectMapper) {
        Person person = getPerson();
        String personJson = "";

        try {
            personJson = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(person);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println(personJson);
    }

    private static Person getPerson() {
        Person person = new Person();
        person.setFirstName("Mike");
        person.setLastName("Hats");
        person.setBirthdate(LocalDate.of(1990,12, 01));
        person.setEmail("mike@mailinator.com");
        person.setGender(Gender.MALE);
        person.setPhoneNumber("3121111111");

        Address homeAddress = new Address();
        homeAddress.setApartmentNumber("1214");
        homeAddress.setStreetAddress("440 Home Lane");
        homeAddress.setCity("Chicago");
        homeAddress.setState("IL");
        homeAddress.setZip("60654");

        Address mailingAddress = new Address();
        mailingAddress.setApartmentNumber("4141");
        mailingAddress.setStreetAddress("800 Mailing Lane");
        mailingAddress.setCity("Chicago");
        mailingAddress.setState("IL");
        mailingAddress.setZip("60626");

        person.setMailingAddress(mailingAddress);
        person.setHomeAddress(homeAddress);

        List<Address> addresses = new ArrayList<>();
        addresses.add(homeAddress);
        addresses.add(mailingAddress);
        person.setAddresses(addresses);

        return person;
    }

    public static String fetchResource(String fileName) throws IOException {
        return new String(Files.readAllBytes(new ClassPathResource(fileName).getFile().toPath()));
    }
}
