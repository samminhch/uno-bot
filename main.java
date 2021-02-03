import uno.*;
import uno.cards.*;

import java.util.*;

public class Main
{
    public static void main(String[] args) {
        //this method will be used to run the entire app ?
        Deck dick = new Deck();
        System.out.println(dick.toString());
        Arrays.stream(new String[] {"1", "2"}).mapToInt(Integer :: parseInt).toArray();
    }
}