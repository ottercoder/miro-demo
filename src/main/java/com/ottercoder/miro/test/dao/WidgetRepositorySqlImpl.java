package com.ottercoder.miro.test.dao;

import com.ottercoder.miro.test.dto.Coordinates;
import com.ottercoder.miro.test.dto.Widget;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
@Profile("sql")
public class WidgetRepositorySqlImpl implements WidgetRepository {

    private final WidgetJpaRepository widgetJpaRepository;

    @Override
    public Widget saveWidget(Widget widget) {
        return widgetJpaRepository.save(widget);
    }

    @Override
    public Widget updateWidget(UUID id, Widget widget) {
        return widgetJpaRepository.save(widget);
    }

    @Override
    public void deleteWidget(UUID id) {
        widgetJpaRepository.deleteById(id);
    }

    @Override
    public Widget getWidget(UUID id) {
        Optional<Widget> optionalWidget = widgetJpaRepository.findById(id);
        return optionalWidget.orElse(null);
    }

    @Override
    public int getHighestZIndex() {
        return widgetJpaRepository.findTop1ByOrderByZDesc().getZ();
    }

    @Override
    public boolean zIndexExists(int z) {
        List<Widget> list = widgetJpaRepository.findAllByZ(z);
        return !list.isEmpty();
    }

    @Override
    public TreeMap<Integer, Widget> getZIndexMap() {
        Map<Integer, Widget> hashMap = widgetJpaRepository.findAllByOrderByZAsc()
            .stream()
            .collect(Collectors.toMap(Widget::getZ, Widget::new));
        return new TreeMap<>(hashMap);
    }

    @Override
    public List<Widget> getWidgetsPaginated(int page, int size) {
        List<Widget> all = widgetJpaRepository.findAllByOrderByZAsc();
        return PaginationUtils.getPageOfList(page, size, all);
    }

    @Override
    public List<Widget> getWidgetsPaginatedByArea(int page, int size, Coordinates downLeft, Coordinates topRight) {
        List<Widget> allInArea = widgetJpaRepository
            .findAllInArea(downLeft.getX(), downLeft.getY(), topRight.getX(), topRight.getY());
        return PaginationUtils.getPageOfList(page, size, allInArea);
    }
}
