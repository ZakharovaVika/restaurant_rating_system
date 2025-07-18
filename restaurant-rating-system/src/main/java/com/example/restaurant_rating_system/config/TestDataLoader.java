package com.example.restaurant_rating_system.config;

import com.example.restaurant_rating_system.enums.CuisineType;
import com.example.restaurant_rating_system.model.Restaurant;
import com.example.restaurant_rating_system.model.Review;
import com.example.restaurant_rating_system.model.Visitor;
import com.example.restaurant_rating_system.service.VisitorService;
import com.example.restaurant_rating_system.service.RestaurantService;
import com.example.restaurant_rating_system.service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component // Делаем этот класс Spring Bean

public class TestDataLoader implements CommandLineRunner {
    private final VisitorService visitorService;
    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    @Autowired
    public TestDataLoader(VisitorService visitorService, RestaurantService restaurantService, ReviewService reviewService) {
        this.visitorService = visitorService;
        this.restaurantService = restaurantService;
        this.reviewService = reviewService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- Загрузка тестовых данных ---");

        // 1. Добавляем Посетителей
        Visitor visitor1 = new Visitor(null, "Alice", 30, "Female");
        Visitor visitor2 = new Visitor(null, "Bob", 25, "Male");
        Visitor visitor3 = new Visitor(null, null, 22, "Male"); // Анонимный посетитель
        Visitor visitor4 = new Visitor(null, "Charlie", 35, "Male");

        visitorService.saveVisitor(visitor1);
        visitorService.saveVisitor(visitor2);
        visitorService.saveVisitor(visitor3);
        visitorService.saveVisitor(visitor4);

        System.out.println("Загружено посетителей: " + visitorService.findAllVisitors().size());

        // 2. Добавляем Рестораны
        Restaurant restaurant1 = new Restaurant(null, "La Piazza", "Authentic Italian cuisine", CuisineType.ITALIAN, new BigDecimal("25.50"), BigDecimal.ZERO);
        Restaurant restaurant2 = new Restaurant(null, "Golden Dragon", "Spicy Chinese dishes", CuisineType.CHINESE, new BigDecimal("18.75"), BigDecimal.ZERO);
        Restaurant restaurant3 = new Restaurant(null, "Le Petit Bistro", "Classic French meals", CuisineType.EUROPEAN, new BigDecimal("35.00"), BigDecimal.ZERO);
        Restaurant restaurant4 = new Restaurant(null, "Sushi Palace", "Fresh Japanese sushi", CuisineType.JAPANESE, new BigDecimal("30.00"), BigDecimal.ZERO);

        restaurantService.saveRestaurant(restaurant1);
        restaurantService.saveRestaurant(restaurant2);
        restaurantService.saveRestaurant(restaurant3);
        restaurantService.saveRestaurant(restaurant4);

        System.out.println("Загружено ресторанов: " + restaurantService.findAllRestaurants().size());

        // 3. Добавляем Оценки (и пересчитываем средний рейтинг)
        // Alice (ID 1) оценивает La Piazza (ID 1)
        reviewService.saveReview(new Review(visitor1.getId(), restaurant1.getId(), 5, "Amazing pasta!"));
        // Bob (ID 2) оценивает La Piazza (ID 1)
        reviewService.saveReview(new Review(visitor2.getId(), restaurant1.getId(), 4, "Good pizza, but a bit noisy."));
        // Charlie (ID 4) оценивает La Piazza (ID 1)
        reviewService.saveReview(new Review(visitor4.getId(), restaurant1.getId(), 5, "Best Italian in town!"));

        // Alice (ID 1) оценивает Golden Dragon (ID 2)
        reviewService.saveReview(new Review(visitor1.getId(), restaurant2.getId(), 4, "Very tasty food, good service."));
        // Bob (ID 2) оценивает Golden Dragon (ID 2)
        reviewService.saveReview(new Review(visitor2.getId(), restaurant2.getId(), 3, "A bit too spicy for me."));

        // Charlie (ID 4) оценивает Le Petit Bistro (ID 3)
        reviewService.saveReview(new Review(visitor4.getId(), restaurant3.getId(), 4, "Elegant atmosphere."));

        // Alice (ID 1) оценивает Sushi Palace (ID 4)
        reviewService.saveReview(new Review(visitor1.getId(), restaurant4.getId(), 5, "Fresh and delicious sushi rolls!"));

        System.out.println("Загружено отзывов: " + reviewService.findAllReviews().size());

        System.out.println("\n--- Тестирование Сервисов ---");

        // Тестирование сервисов:
        // Получение всех ресторанов
        List<Restaurant> allRestaurants = restaurantService.findAllRestaurants();
        System.out.println("Все рестораны:");
        allRestaurants.forEach(System.out::println);

        // Получение ресторана по ID и его обновленной оценки
        System.out.println("\nПроверка оценки La Piazza (ID 1):");
        restaurantService.findRestaurantById(restaurant1.getId()).ifPresent(r -> {
            System.out.println("Ресторан: " + r.getName() + ", Оценка: " + r.getUserRating()); // Должно быть (5+4+5)/3 = 4.67
        });

        System.out.println("\nПроверка оценки Golden Dragon (ID 2):");
        restaurantService.findRestaurantById(restaurant2.getId()).ifPresent(r -> {
            System.out.println("Ресторан: " + r.getName() + ", Оценка: " + r.getUserRating()); // Должно быть (4+3)/2 = 3.50
        });

        // Поиск отзывов для ресторана
        System.out.println("\nОтзывы о La Piazza (ID 1):");
        reviewService.findReviewsByRestaurantId(restaurant1.getId()).forEach(System.out::println);

        // Поиск отзывов от посетителя
        System.out.println("\nОтзывы от Alice (ID 1):");
        reviewService.findReviewsByVisitorId(visitor1.getId()).forEach(System.out::println);

        // Удаление отзыва и проверка пересчета
        System.out.println("\nУдаление отзыва Alice (ID 1) о La Piazza (ID 1)...");
        reviewService.removeReview(visitor1.getId(), restaurant1.getId());

        System.out.println("Проверка оценки La Piazza (ID 1) после удаления отзыва:");
        restaurantService.findRestaurantById(restaurant1.getId()).ifPresent(r -> {
            System.out.println("Ресторан: " + r.getName() + ", Оценка: " + r.getUserRating()); // Должно быть (4+5)/2 = 4.50
        });

        // Тестирование удаления ресторана
        System.out.println("\nУдаление ресторана Golden Dragon (ID 2)...");
        restaurantService.removeRestaurant(restaurant2.getId());

        System.out.println("Проверка наличия Golden Dragon после удаления:");
        restaurantService.findRestaurantById(restaurant2.getId()).ifPresentOrElse(
                r -> System.out.println("Ресторан " + r.getName() + " найден (ошибка!)"),
                () -> System.out.println("Ресторан Golden Dragon успешно удален.")
        );

        System.out.println("\n--- Загрузка тестовых данных завершена ---");
    }
}
