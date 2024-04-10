package db;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TaskCategory {
    NO_CATEGORY, ENGINEERING, SALES, DOCUMENTATION, WEB_DESIGN, TESTING;

    public static List<String> listOfValues() {
        return Arrays.stream(TaskCategory.values()).map(TaskCategory::name).collect(Collectors.toList());
    }
}