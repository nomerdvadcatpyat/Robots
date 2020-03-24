package gui.streams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ObjectStreamCreator {
    public static ObjectOutputStream createStream(File file) throws IOException {
        return file.exists()
                ? new AppendingObjectOutputStream(new FileOutputStream(file, true))
                : new ObjectOutputStream(new FileOutputStream(file, true));
    }
}
