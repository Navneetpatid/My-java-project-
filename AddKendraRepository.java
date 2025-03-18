import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

@Component
public class BindingValidationUtil {

    public static ResponseEntity<Object> requestValidation(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        Map<String, Object> map = new HashMap<>();

        for (ObjectError error : bindingResult.getAllErrors()) {
            errorMessage.append(error.getDefaultMessage()).append(". ");
        }

        map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
        map.put(DataConstant.MESSAGE, errorMessage.toString());
        map.put(DataConstant.RESPONSE_BODY, null);

        return ResponseEntity.ok(map);
    }
}
