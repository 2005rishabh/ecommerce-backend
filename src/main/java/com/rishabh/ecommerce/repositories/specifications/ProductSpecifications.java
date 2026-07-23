package com.rishabh.ecommerce.repositories.specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.rishabh.ecommerce.entities.Product;

import jakarta.persistence.criteria.Predicate;

public class ProductSpecifications {
    public static Specification<Product> buildSpecification(String category, Double minPrice, Double maxPrice, String keyword) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(category != null && !category.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("category")),
                    category.toLowerCase().trim()
                ));
            }

            if(minPrice != null) { 
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }


            if(maxPrice != null) { 
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if(keyword != null && !keyword.trim().isEmpty()) {
                String searchTerm = "%" + keyword.toLowerCase().trim() + "%";
                Predicate nameLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), searchTerm);
                Predicate descLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchTerm);
                predicates.add(criteriaBuilder.or(nameLike, descLike));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }
    }
}
