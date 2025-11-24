package abstractions;

import java.nio.file.Path;
import java.util.Map;

public interface OutputStats {

    void outputToFile(Map<String, Long> stats, String attribute, Path rootPath);

}
