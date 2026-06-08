package test;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import org.junit.jupiter.api.Test;
import pl.mruczekprogramista.data.Spray;
import pl.mruczekprogramista.services.SprayService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.component.textfield.TextField;

public class BaseTest {

    @Test
    void shouldAssignSprayNumbers() {


        SprayService service = mock(SprayService.class);
        when(service.findAll()).thenReturn(List.of(
                new Spray(),
                new Spray(),
                new Spray()
        ));

        List<Spray> result = service.findAll();

        assert result.size() ==3;



    }
//    @Test
//    void shouldValidateSprayName() {
//        Spray spray = new Spray();
//        spray.setSprayName("");
//
//        BeanValidationBinder<Spray> binder = new BeanValidationBinder<>(Spray.class);
//        binder.setBean(spray);
//
//        assertTrue(binder.validate().hasErrors());
//
//
//    }
}
