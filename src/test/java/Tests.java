
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class Tests {

    @Test
    @Tag("smoke")
    public void testS(){
        System.out.println("smoke");
    }

    @Test
    @Tag("regress")
    public void testR(){
        System.out.println("regress");
    }
}
