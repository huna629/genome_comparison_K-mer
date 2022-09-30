import java.io.Serializable;

public class Sequence implements Serializable {

      long key;
      int value;
      Sequence next;
      private static final long serialVersionUID = 5462223600l;

      Sequence(long key, int value) {
            this.key = key;
            this.value = value;
            this.next = null;
      }

      public int getValue() {
            return value;
      }

      public void setValue(int value) {
            this.value = value;
      }

      public long getKey() {
            return key;
      }

      public Sequence getNext() {
            return next;
      }

      public void setNext(Sequence next) {
            this.next = next;
      }
      
      public int hashCode() {
    	    long rem=key%200000;
    	    return (int) rem;
      }

}
