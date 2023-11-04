package com.example.coffe14;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
Spring - библиотека для создания серверных приложений
@Controller @RestController - компонент который взаимодействует с клиентами
@Service @Component - компоненты приложения реализующие бизнес-логику
@Repository - компонент для взаимодействия с базой данных
    Hibernate/JPA - Object-Relational Mapping - напрямую спасать и поднимать из базы данных экземпляры классов
@Bean
@Configuration

Spring boot - дополнительная надостройка на Spring которая позволяет создавать серверные приложения еще быстрее и проще

RestController - принимаются http-запросы из вне ответами на них являются экземпляры каких-либо объектов
которые будут сериализованы в json

REST - Remote State Transfer

HTTP - hypertext transfer protocol
    GET -       SELECT
    PUT -       INSERT
    POST -      UPDATE
    DELETE -    DELETE

 */

// @RestController - контроллер который принимает и отдает клиентам экземпляры
// каких-то внутренних классов проекта
@RestController
public class CoffeeController {
    private List<Coffee> coffees = new ArrayList<>();

    public CoffeeController() {
        coffees.addAll(
                Arrays.asList(
                        new Coffee("Cappuccino", new BigDecimal(3.0)),
                        new Coffee("Latte", new BigDecimal(3.5)),
                        new Coffee("Espresso", new BigDecimal(1.5)),
                        new Coffee("Americano", new BigDecimal(1.2)),
                        new Coffee("Ristretto", new BigDecimal(3.6))));
    }

    // http://localhost:8080/coffees
    @GetMapping("/coffees") // HTTP GET http://localhost:8080/coffees
    public Iterable<Coffee> getCoffees() {
        return coffees;
    }

    // http://localhost:8080/coffees/123
    @GetMapping("/coffees/{id}")
    public Optional<Coffee> getCoffeeById(@PathVariable("id") String id) {
        return coffees.stream()
                .filter(coffee -> coffee.getId().equals(id))
                .findFirst();
    }

    // DELETE http://localhost:8080/coffees/{id}
    @DeleteMapping("/coffees/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        if (coffees.removeIf(coffee -> coffee.getId().equals(id))) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST http://localhost:8080/coffees
    // json с параметрами кофе который нужно создать будет передаваться в теле запроса
    @PutMapping("/coffees")
    public Coffee create(@RequestBody Coffee coffee) {
        coffees.add(coffee);
        return coffee;
    }

    // POST http://localhost:8080/coffees/{id}
    // @RequestBody - json с параметрами кофе который нужно обновить будет передаваться в теле запроса
    @PostMapping("/coffees/{id}")
    public Coffee update(@PathVariable String id, @RequestBody Coffee coffee) {
        for (int i = 0; i < coffees.size(); i++) {
            if (coffees.get(i).getId().equals(id)) {
                coffees.set(i, coffee);
                return coffee;
            }
        }
        return null;
    }

    // GET http://localhost:8080/find?text=ap
    // @RequestParam указывает на переменную из http запроса
    // вернуть все кофе в названии которых содержится подстрока "ap"
    @GetMapping("/find")
    public Iterable<Coffee> find(@RequestParam("text") String text){
        return coffees.stream()
                .filter(coffee -> coffee.getName().contains("ap"))
                .collect(Collectors.toList());
    }

    // http://localhost:8080/between?from=1.2&to=3.4
    @GetMapping("/between")
    public Iterable<Coffee> between (@RequestParam("from") BigDecimal from, @RequestParam("to") BigDecimal to){
        return coffees.stream()
                .filter(coffee -> coffee.getPrice().doubleValue() >= from.doubleValue()
                        &&  coffee.getPrice().doubleValue() <= to.doubleValue())
                .collect(Collectors.toList());
    }

}
