import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class Tests {

    @Test
    @Tag("smoke")
    public void smokeTest(){
        System.out.println("smoke");
    }

    @Test
    @Tag("regress")
    public void regressTest(){
        System.out.println("regress");
    }
}
