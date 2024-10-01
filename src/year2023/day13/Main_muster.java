//package days.day13;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.apache.commons.text.similarity.LevenshteinDistance;
//
//import utils.ImportUtils;
//import utils.Utils;
//
//public class Main_muster {
//
//    public static void main(String[] args) throws IOException {
//        //    final String filePath = System.getProperty("user.dir") + "/resources/days/day13/input_13_test_01.txt";
//        //   final String filePath = System.getProperty("user.dir") + "/resources/days/day13/input_13_test_02.txt";
//        final String filePath = System.getProperty("user.dir") + "/resources/days/day13/input_13.txt";
//
//        String inputs = readFileAsString(filePath);
//
//        int result = detectMirrors(inputs, 0);
//        Utils.log("Total result: "+result);
//
//    }
//
//    public static String readFileAsString(String filePath) throws IOException {
//        return new String(Files.readAllBytes(Paths.get(filePath)));
//    }
//
//
//    public static int detectMirrors(String input, int allowedErrors) {
//        var patterns = input.trim().split("\n\n");
//        var sum = 0;
//        var ld = LevenshteinDistance.getDefaultInstance();
//
//        for (var pattern: patterns) {
//            var lines = pattern.split("\n");
//            var columnsList = new ArrayList<List<Character>>();
//
//            for (var line: lines) {
//                for (var x = 0; x < line.length(); x++) {
//                    if (columnsList.size() < x + 1) {
//                        columnsList.add(new ArrayList<>());
//                    }
//                    columnsList.get(x).add(line.charAt(x));
//                }
//            }
//
//            var columns = columnsList.stream()
//                    .map(column -> column.stream().map(String::valueOf).collect(Collectors.joining()))
//                    .toList();
//
//            for (var i = 0; i < lines.length - 1; i++) {
//                var lDistance = 0;
//
//                for (int j = i, k = i + 1; j >= 0 && k < lines.length && lDistance <= allowedErrors; j--, k++) {
//                    lDistance += ld.apply(lines[j], lines[k]);
//                }
//
//                if (lDistance == allowedErrors) {
//                    sum += 100 * (i + 1);
//                    break;
//                }
//            }
//
//            for (var i = 0; i < columns.size() - 1; i++) {
//                var lDistance = 0;
//
//                for (int j = i, k = i + 1; j >= 0 && k < columns.size() && lDistance <= allowedErrors; j--, k++) {
//                    lDistance += ld.apply(columns.get(j), columns.get(k));
//                }
//
//                if (lDistance == allowedErrors) {
//                    sum += i + 1;
//                    break;
//                }
//            }
//        }
//
//        return sum;
//    }
//
//}