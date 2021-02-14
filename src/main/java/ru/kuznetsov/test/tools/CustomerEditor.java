package ru.kuznetsov.test.tools;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kuznetsov.test.entities.Customer;
import ru.kuznetsov.test.repositories.CustomerRepository;

@SpringComponent
@UIScope
public class CustomerEditor extends VerticalLayout implements KeyNotifier {

    private final CustomerRepository repository;
    private Customer customer;
    private ChangeHandler changeHandler;

    TextField name = new TextField("Имя");
    RadioButtonGroup<String> gender = new RadioButtonGroup<>();
    DatePicker birthday = new DatePicker();

    Button save = new Button("Сохранить", VaadinIcon.CHECK.create());
    Button cancel = new Button("Отмена");
    Button delete = new Button("Удалить", VaadinIcon.TRASH.create());

    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Customer> binder = new Binder<>(Customer.class);

    @Autowired
    public CustomerEditor(CustomerRepository repository) {
        this.repository = repository;

        gender.setLabel("Пол");
        gender.setItems("Мужской", "Женский");
        gender.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        gender.setValue("Мужской");

        add(name, gender, birthday, actions);
        binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");
        addKeyPressListener(Key.ENTER, e -> save());
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editCustomer(customer));

        setVisible(false);
    }

    void delete() {
        repository.delete(customer);
        changeHandler.onChange();
    }

    void save() {
        repository.save(customer);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editCustomer(Customer editedCustomer) {
        if (editedCustomer == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = editedCustomer.getId() != null;

        if (persisted) {
            customer = repository.findById(editedCustomer.getId()).get();
        } else {
            customer = editedCustomer;
        }

        cancel.setVisible(persisted);
        binder.setBean(customer);

        setVisible(true);

        name.focus();
    }

    public void setChangeHandler(ChangeHandler handler) {
        changeHandler = handler;
    }

}
