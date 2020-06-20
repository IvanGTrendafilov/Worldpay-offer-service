package com.trendafilov.ivan.worldpay.offerservice.repositories;

import com.trendafilov.ivan.worldpay.offerservice.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByOfferId(Long offerId);
}
