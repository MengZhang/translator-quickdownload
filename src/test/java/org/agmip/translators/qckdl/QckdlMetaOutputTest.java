package org.agmip.translators.qckdl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 *
 * @author Meng Zhang
 */
public class QckdlMetaOutputTest {

    QckdlMetaOutput output;
    URL resource;
    URL expectedRes;
    String fileName = "metadata.json";

    @Before
    public void setUp() throws Exception {
        output = new QckdlMetaOutput();
        resource = this.getClass().getResource("/" + fileName);
    }

    @Test
    public void test() throws IOException, Exception {

        InputStream is = new FileInputStream(resource.getPath());
        OutputStream os = new FileOutputStream("metadata.csv");
        output.writeFile(os, is);
        os.close();
        is.close();
    }
}
