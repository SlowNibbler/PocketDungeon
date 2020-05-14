package edu.tacoma.uw.myang12.pocketdungeon.model;


        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class CharacterContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Character> ITEMS = new ArrayList<Character>();

    /**
     * A map of sample (course) items, by ID.
     */
    public static final Map<String, Character> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

//    static {
//        // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createCourseItem(i));
//        }
//    }

    private static void addItem(Character item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getCharacterName(), item);
        System.out.println("add: " + item.toString());
    }

    private static Character createCourseItem(int position) {
        return new Character("Name" + position, "class" + position, "race" + position, "level" + position,
                                "str" + position, "dex" + position, "const" + position, "int" + position, "wis" + position, "cha" + position);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Course: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

//    /**
//     * A dummy item representing a piece of content.
//     */
//    public static class CourseItem {
//        public final String id;
//        public final String content;
//        public final String details;
//
//        public CoureseItem(String id, String content, String details) {
//            this.id = id;
//            this.content = content;
//            this.details = details;
//        }
//
//        @Override
//        public String toString() {
//            return content;
//        }
//    }

}
