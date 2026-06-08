package pl.mruczekprogramista.views.masterdetail;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.*;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import pl.mruczekprogramista.data.Spray;
import pl.mruczekprogramista.services.SprayService;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

import java.util.List;


@PageTitle("Opryski Data miejsce nazwa środka długość karencji")
@Route("/:sampleAddressID?/:action?(edit)")
@Menu(order = 0, icon = LineAwesomeIconUrl.COLUMNS_SOLID)
@RouteAlias("")
public class MasterDetailView extends Div implements BeforeEnterObserver {

    private final String SAMPLEADDRESS_ID = "sampleAddressID";
    private final String SAMPLEADDRESS_EDIT_ROUTE_TEMPLATE = "/%s/edit";

    private final Grid<Spray> grid = new Grid<>(Spray.class, false);

    private IntegerField operationNumber;
    private DatePicker madeDate;
    private ComboBox<String> place;
    private TextField sprayName;
    private TextField plantName;
    private TextField gracePeriod;

    private final Button cancel = new Button("Wyczyść");
    private final Button save = new Button("Zapisz");
    private final Button delete = new Button("Usuń");

    private final BeanValidationBinder<Spray> binder;

    private Spray spray;

    private final SprayService sprayService;

    public MasterDetailView(SprayService sprayService) {
        this.sprayService = sprayService;
        addClassNames("opryski", "data", "miejsce");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSplitterPosition(75);

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        List<Spray> sprays =sprayService.findAll();

        for (int i =0; i < sprays.size(); i++) {
            sprays.get(i).setSprayNumber(i +1);
        }
        grid.removeAllColumns();
        grid.setItems(sprays);



        grid.addColumn(Spray::getSprayNumber)
                .setHeader("Liczba porządkowa")
                .setSortable(true)
                .setAutoWidth(true);
        grid.addColumn(Spray::getMadeDate)
                .setHeader("Data zabiegu")
                .setSortable(true)
                .setAutoWidth(true);

        grid.addColumn(Spray::getPlace)
                .setHeader("Miejsce")
                .setSortable(true)
                .setAutoWidth(true);
        grid.addColumn(Spray::getSprayName)
                .setHeader("Nazwa środka")
                .setSortable(true)
                .setAutoWidth(true);
        grid.addColumn(Spray::getPlantName)
                .setHeader("Roślina")
                .setSortable(true)
                .setAutoWidth(true);
        grid.addColumn(Spray::getGracePeriod)
                .setHeader("Okres karencji w dniach")
                .setSortable(true)
                .setAutoWidth(true);

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            Spray selected = event.getValue();
            if (selected != null) {
                populateForm(selected);

                UI.getCurrent().navigate(String.format(
                        SAMPLEADDRESS_EDIT_ROUTE_TEMPLATE, selected.getId()
                ));
            } else  {
                clearForm();
                UI.getCurrent().navigate(MasterDetailView.class);
            }


        });

        // Configure Form
        binder = new BeanValidationBinder<>(Spray.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.spray == null) {
                    this.spray = new Spray();
                }
                binder.writeBean(this.spray);
                sprayService.save(this.spray);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(MasterDetailView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated " +
                                "the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Błąd uzupełnienia danych, sprawdź dane i spóbuj ponownie ");
            }
        });
        save.addClickShortcut(Key.ENTER);



        delete.addClickListener(e -> {
            if (spray == null) {
                Notification.show("Wybierz rekord z tabeli");
                return;
            }
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Usówanie zabiegu");
            dialog.setText("Czy na pewno chcesz usunąć");

            dialog.setCancelable(true);
            dialog.setCancelText("Anuluj");

            dialog.setConfirmText("Usuń");

            dialog.addConfirmListener(event -> {
                Long id = spray.getId();

                if (id == null) {
                    Notification.show("Brak ID");
                    return;
                }
                sprayService.deleteByid(id);

                clearForm();
                refreshGrid();

                Notification.show("Zabieg usunęty");

                UI.getCurrent().navigate(MasterDetailView.class);
            });

            dialog.open();


        });

    }

//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        Optional<Long> sampleAddressId = event.getRouteParameters().get(SAMPLEADDRESS_ID).map(Long::parseLong);
//        if (sampleAddressId.isPresent()) {
//            Optional<Spray> sampleAddressFromBackend = sampleAddressService.get(sampleAddressId.get());
//            if (sampleAddressFromBackend.isPresent()) {
//                populateForm(SprayFromBackend.get());
//            } else {
//                Notification.show(
//                        String.format("The requested sampleAddress was not found, ID = %s", sampleAddressId.get()),
//                        3000, Notification.Position.BOTTOM_START);
//                // when a row is selected but the data is no longer available,
//                // refresh grid
//                refreshGrid();
//                event.forwardTo(MasterDetailView.class);
//            }
//        }
//    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);
        editorDiv.getStyle().set("padding", "20px");

        FormLayout formLayout = new FormLayout();
        formLayout.setMaxWidth("500px");
        formLayout.getStyle().set("margin-left", "auto");

        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0",1)
        );

        madeDate = new  DatePicker("Data");

        place = new ComboBox<>("Miejsce");
        place.setItems("Pole","Folia");
        place.setPlaceholder("Wybierz miejsce");

        sprayName = new TextField("Podaj nazwę środka ");
        sprayName.setPlaceholder("Nazwa środka");

        plantName = new TextField("Podaj nazwę rośliny");
        plantName.setPlaceholder("Nazwa roślny");

        gracePeriod = new TextField("Karencja");
        gracePeriod.setPlaceholder("Długość karencji w dniach ");

        formLayout.add( madeDate, place, sprayName, plantName, gracePeriod);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setMaxWidth("400px");
        buttonLayout.getStyle().set("margin-left", "auto");
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        buttonLayout.add(save, cancel, delete);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        List<Spray> sprays = sprayService.findAll();

        for (int i =0; i< sprays.size(); i++) {
            sprays.get(i).setSprayNumber(i + 1);
        }
        grid.setItems(sprays);
        grid.select(null);
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Spray value) {
        this.spray = value;
        binder.readBean(this.spray);

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

    }
}
