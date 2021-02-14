package com.ottercoder.miro.test.dao;

import com.ottercoder.miro.test.dto.Widget;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WidgetJpaRepository extends JpaRepository<Widget, UUID> {

    Widget findTop1ByOrderByZDesc();

    List<Widget> findAllByZ(int z);

    List<Widget> findAllByOrderByZAsc();

    @Query(
        value = "SELECT * FROM WIDGET w WHERE w.x >= ?1 + w.width AND w.y >= ?2 + w.height AND w.x <= ?3 - w.width AND w.y <= ?4 -w.height",
        nativeQuery = true)
    List<Widget> findAllInArea(int xLeft, int yLow, int xRight, int yTop);
}
