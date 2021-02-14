package ru.kuznetsov.test.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;
import ru.kuznetsov.test.entities.Customer;
import ru.kuznetsov.test.repositories.CustomerRepository;
import ru.kuznetsov.test.tools.CustomerEditor;

import java.time.LocalDate;

@Route
public class MainView extends VerticalLayout {

    private final CustomerRepository repo;
    private final CustomerEditor editor;
    private final Button addNewBtn;

    final Grid<Customer> grid;
    final TextField filter;

    public MainView(CustomerRepository repo, CustomerEditor editor) {
        this.repo = repo;
        this.editor = editor;
        this.grid = new Grid<>(Customer.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("Новый покупатель", VaadinIcon.PLUS.create());

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);

        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("id", "name", "gender", "birthday");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        filter.setPlaceholder("Фильтрация по имени");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editCustomer(e.getValue());
        });

        addNewBtn.addClickListener(e -> editor.editCustomer(new Customer("", "", LocalDate.now())));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listCustomers(filter.getValue());
        });

        listCustomers(null);
    }

    void listCustomers(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(repo.findAll());
        } else {
            grid.setItems(repo.findByNameStartsWithIgnoreCase(filterText));
        }
    }

}