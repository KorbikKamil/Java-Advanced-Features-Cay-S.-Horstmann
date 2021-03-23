package serialClone;

import java.io.*;

/**
 * Klasa której metoda clone wykorzystuje serializację
 */
class SerialCloneable implements Cloneable, Serializable {
    public Object clone() throws CloneNotSupportedException{
        try{
            //Zapisuje obiekt w tablicy bajtów
            var bout = new ByteArrayOutputStream();
            try(var out = new ObjectOutputStream(bout)){
                out.writeObject(this);
            }

            //Wczytuje klon obiektu z tablicy bajtów
            try(var bin = new ByteArrayInputStream(bout.toByteArray())){
                var in = new ObjectInputStream(bin);
                return in.readObject();
            }
        } catch (IOException | ClassNotFoundException e){
            var e2 = new CloneNotSupportedException();
            e2.initCause(e);
            throw e2;
        }
    }
}
