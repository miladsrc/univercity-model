package baseUtil.ExceptionUtil;

public class ChoiceNotFoundException extends RuntimeException {
    public ChoiceNotFoundException(String message) {
        System.out.println ("choice not found please try again");
    }
}