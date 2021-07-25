package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository for manipulating tag data in database
 */
@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {

    Optional<Tag> findTagByName(String name);

    boolean existsByName(String name);

    Page<Tag> getTagsByNameLike(String namePart, Pageable pageable);

    @Query(value = "SELECT t.id, t.name, COUNT(t.id) as t_count from users u" +
            " join orders o on u.id = o.users_id" +
            " join gift_certificate gc on gc.id = o.certificate_id" +
            " join m2m_certificate_tag m2mct on gc.id = m2mct.cert_id" +
            " join tag t on t.id = m2mct.tag_id where u.id =" +
            " (SELECT s.u_id from (SELECT SUM(o.cost) as s_cost, u.id as u_id from orders o" +
            " join users u on u.id = o.users_id group by u_id order by s_cost DESC LIMIT 1) s)" +
            " group by t.name order by t_count desc LIMIT 1", nativeQuery = true)
    Tag getMostUsedTagOfValuableCustomer ();

}
