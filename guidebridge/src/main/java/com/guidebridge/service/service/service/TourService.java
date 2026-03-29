package com.guidebridge.service;

import com.guidebridge.model.State;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * TourService — Single Service for the Easetour Application
 *
 * Contains all backend Java logic in one place (student-friendly):
 *
 *  1. STATE DATA        — tourism data for 6 Indian states
 *  2. USER AUTH         — SHA-256 password hashing + in-memory user store
 *  3. INPUT VALIDATION  — Java regex for username and password rules
 *  4. SEARCH            — Java Stream filter + Comparator ranking
 *  5. VISIT COUNTER     — ConcurrentHashMap<String, AtomicInteger> tracker
 */
@Service
public class TourService {

    // ═══════════════════════════════════════════════════════════════════════════
    // 1. STATE DATA
    // ═══════════════════════════════════════════════════════════════════════════

    /** Ordered map of state id → State object. LinkedHashMap preserves insertion order. */
    private final Map<String, State> stateMap = new LinkedHashMap<>();

    private void initializeStates() {
        stateMap.put("goa", new State(
            "goa", "Goa", "The Pearl of the Arabian Sea",
            "Goa, located on the western coast of India along the Arabian Sea, is famous for its beautiful beaches, vibrant nightlife, Portuguese heritage, and scenic landscapes. From the lively shores of Baga and Calangute to the peaceful charm of Palolem, Goa offers relaxation, adventure, culture, and delicious seafood.",
            "https://janakitours.com/wp-content/uploads/2025/01/Goa.png",
            "November to February (Pleasant Winter Season)",
            Arrays.asList("Baga Beach", "Calangute Beach", "Anjuna Beach", "Palolem Beach", "Basilica of Bom Jesus", "Fort Aguada", "Dudhsagar Waterfalls"),
            Arrays.asList("Goan Fish Curry", "Prawn Balchão", "Vindaloo", "Bebinca", "Sorpotel", "Feni"),
            "Portuguese heritage, Carnival Festival, Beach festivals, Seafood cuisine, Music and nightlife traditions",
            "Beach hopping, Water sports (parasailing, jet ski, scuba diving), Cruise on Mandovi River, Visit churches and forts, Explore flea markets.",
            "GOA Tourism"
        ));
        stateMap.put("kerala", new State(
            "kerala", "Kerala", "God's Own Country",
            "Kerala, nestled between the Western Ghats and the Arabian Sea, is celebrated for its serene backwaters, lush tea and spice plantations, rich cultural traditions, and pristine beaches. From the tranquil houseboats of Alleppey to the misty hills of Munnar and the wildlife sanctuaries of Periyar, Kerala soothes the soul.",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Backwaters_kerala.jpg/1200px-Backwaters_kerala.jpg",
            "October to March (Cool and Pleasant)",
            Arrays.asList("Alleppey Backwaters", "Munnar Tea Gardens", "Kovalam Beach", "Periyar Wildlife Sanctuary", "Thrissur Pooram", "Wayanad Hills", "Fort Kochi"),
            Arrays.asList("Kerala Fish Curry", "Appam with Stew", "Puttu and Kadala", "Sadya", "Karimeen Pollichathu", "Banana Chips"),
            "Kathakali dance, Theyyam rituals, Onam festival, Ayurvedic traditions, Boat races",
            "Houseboat rides in Alleppey, Trekking in Munnar, Wildlife safari in Periyar, Ayurvedic spa, Kathakali performances, Spice plantation tours.",
            "KERALA Tourism"
        ));
        stateMap.put("punjab", new State(
            "punjab", "Punjab", "The Land of Five Rivers",
            "Punjab, the breadbasket of India, is a land of vibrant culture, rich history, and warm hospitality. Home to the sacred Golden Temple in Amritsar, colorful Bhangra dances, and mouth-watering cuisine. From the Wagah Border ceremony to the historic Anandpur Sahib, every corner tells a story of valor and devotion.",
            "https://upload.wikimedia.org/wikipedia/commons/9/9e/Golden_Temple_reflected_in_the_holy_sarovar.jpg",
            "October to March (Cool and Comfortable)",
            Arrays.asList("Golden Temple Amritsar", "Wagah Border", "Jallianwala Bagh", "Anandpur Sahib", "Rock Garden Chandigarh", "Qila Mubarak Patiala", "Durgiana Temple"),
            Arrays.asList("Butter Chicken", "Dal Makhani", "Sarson da Saag with Makki Roti", "Lassi", "Amritsari Kulcha", "Pinni"),
            "Bhangra dance, Gidda, Lohri & Baisakhi festivals, Sikh heritage, Dhabas culture",
            "Visit the Golden Temple at dawn, Witness the Wagah Border ceremony, Attend Baisakhi celebrations, Try authentic Punjabi dhaba food, Explore Rock Garden.",
            "PUNJAB Tourism"
        ));
        stateMap.put("rajasthan", new State(
            "rajasthan", "Rajasthan", "The Land of Kings",
            "Rajasthan, India's largest state, is a majestic tapestry of golden deserts, towering forts, ornate palaces, and vibrant folk traditions. The Pink City of Jaipur dazzles with its Amber Fort; Jodhpur's Mehrangarh Fort is breathtaking; and romantic Udaipur enchants all who visit. Camel safaris in the Thar Desert complete this royal journey.",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/3/34/Amber_Fort_near_Jaipur%2C_Rajasthan%2C_India.jpg/1200px-Amber_Fort_near_Jaipur%2C_Rajasthan%2C_India.jpg",
            "October to March (Cool Desert Winters)",
            Arrays.asList("Amber Fort Jaipur", "Mehrangarh Fort Jodhpur", "City Palace Udaipur", "Jaisalmer Fort", "Hawa Mahal", "Ranthambore National Park", "Thar Desert"),
            Arrays.asList("Dal Baati Churma", "Laal Maas", "Ghevar", "Ker Sangri", "Mirchi Bada", "Mawa Kachori"),
            "Rajput heritage, Pushkar Camel Fair, Teej & Gangaur festivals, folk music (Manganiyar), puppet shows",
            "Camel safari in Thar Desert, Explore Amber Fort, Hot air balloon over Jaipur, Village heritage walk, Watch folk dance performances, Sunrise over Jaisalmer Fort.",
            "RAJASTHAN Tourism"
        ));
        stateMap.put("uttar-pradesh", new State(
            "uttar-pradesh", "Uttar Pradesh", "The Heartland of India",
            "Uttar Pradesh is India's most populous and historically significant state. Home to the iconic Taj Mahal in Agra, the spiritual ghats of Varanasi, the birthplace of Lord Krishna in Mathura, and the enlightenment site of Lord Buddha in Sarnath. Lucknow's Nawabi architecture and fragrant biryanis add cultural richness.",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/da/Taj-Mahal.jpg/1200px-Taj-Mahal.jpg",
            "October to March (Best Weather)",
            Arrays.asList("Taj Mahal Agra", "Varanasi Ghats", "Mathura & Vrindavan", "Lucknow Bara Imambara", "Sarnath", "Fatehpur Sikri", "Agra Fort"),
            Arrays.asList("Lucknowi Biryani", "Tunday Kababi", "Peda from Mathura", "Chaat from Varanasi", "Bedmi Puri", "Shahi Tukda"),
            "Nawabi culture of Lucknow, Dev Deepawali in Varanasi, Holi in Mathura, Buddhist heritage in Sarnath",
            "Sunrise at Taj Mahal, Ganga Aarti in Varanasi, Boat ride on the Ganga, Explore Lucknow's Nawabi lanes, Holi in Mathura, Visit Fatehpur Sikri.",
            "UTTAR PRADESH Tourism"
        ));
        stateMap.put("uttarakhand", new State(
            "uttarakhand", "Uttarakhand", "The Land of Gods",
            "Uttarakhand, nestled in the Himalayas, is a state of breathtaking natural beauty and deep spiritual significance. Known as 'Devbhoomi', it is home to the sacred Char Dham pilgrimage sites. The adventure hub of Rishikesh offers world-class river rafting and yoga retreats; the Valley of Flowers bursts with alpine blooms.",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/6/69/Kedarnath_Temple.jpg/1200px-Kedarnath_Temple.jpg",
            "April to June & September to November",
            Arrays.asList("Char Dham (Badrinath, Kedarnath, Gangotri, Yamunotri)", "Rishikesh", "Jim Corbett National Park", "Nainital Lake", "Mussoorie", "Valley of Flowers", "Auli Ski Resort"),
            Arrays.asList("Kafuli", "Chainsoo", "Bal Mithai", "Aloo Ke Gutke", "Bhang Ki Chutney", "Mandua Ki Roti"),
            "Hindu pilgrimage traditions, Haridwar Kumbh Mela, Yoga & meditation in Rishikesh, folk dance Chholiya",
            "River rafting in Rishikesh, Char Dham Yatra, Trekking in Valley of Flowers, Skiing in Auli, Ganga Aarti in Haridwar, Wildlife safari in Jim Corbett.",
            "UTTARAKHAND Tourism"
        ));
    }

    /** Returns all states as an ordered list. */
    public List<State> getAllStates() {
        return List.copyOf(stateMap.values());
    }

    /** Returns a single state by its slug id (e.g. "goa"). */
    public Optional<State> getStateById(String id) {
        return Optional.ofNullable(stateMap.get(id.toLowerCase()));
    }


    // ═══════════════════════════════════════════════════════════════════════════
    // 2. USER AUTH  —  SHA-256 hashing + in-memory user store
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * User store: username → String[]{passwordHash, displayName}
     * Using String[] instead of a separate User class saves a file.
     *
     * Index:  [0] = SHA-256 password hash
     *         [1] = display name
     */
    private final Map<String, String[]> users = new HashMap<>();

    /**
     * Hashes a plain-text password using SHA-256 (java.security.MessageDigest).
     * This is real cryptographic hashing — much better than storing plain text.
     */
    public String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            // Convert byte[] to hex string manually — no external libraries needed
            StringBuilder hex = new StringBuilder();
            for (byte b : bytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    /**
     * Registers a new user after validation passes.
     * Returns null on success, or an error message String on failure.
     */
    public String register(String username, String password, String displayName) {
        String error = validateInputs(username, password, displayName);
        if (error != null) return error;

        String key = username.toLowerCase().trim();
        if (users.containsKey(key)) {
            return "Username '" + username + "' is already taken.";
        }
        users.put(key, new String[]{ sha256(password), displayName.trim() });
        return null; // null = success
    }

    /**
     * Authenticates a login attempt.
     * Returns the display name on success, or null on failure.
     */
    public String authenticate(String username, String password) {
        String[] userData = users.get(username.toLowerCase().trim());
        if (userData == null) return null;                     // user not found
        if (!sha256(password).equals(userData[0])) return null; // wrong password
        return userData[1]; // return display name on success
    }

    /** Returns the display name for a given username, or null if not found. */
    public String getDisplayName(String username) {
        String[] data = users.get(username.toLowerCase());
        return data != null ? data[1] : null;
    }

    /** Returns total number of registered users. */
    public int getUserCount() { return users.size(); }


    // ═══════════════════════════════════════════════════════════════════════════
    // 3. INPUT VALIDATION  —  Java regex (Pattern) rules
    // ═══════════════════════════════════════════════════════════════════════════

    // Compiled once — efficient for repeated use
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final Pattern HAS_LETTER       = Pattern.compile(".*[a-zA-Z].*");
    private static final Pattern HAS_DIGIT        = Pattern.compile(".*[0-9].*");

    /**
     * Validates all registration inputs using Java Pattern/regex.
     * Returns the first error message found, or null if everything is valid.
     */
    public String validateInputs(String username, String password, String displayName) {
        // Username: 3–20 chars, letters/numbers/underscore only
        if (username == null || username.isBlank())
            return "Username cannot be empty.";
        if (!USERNAME_PATTERN.matcher(username.trim()).matches())
            return "Username: 3–20 chars, letters, numbers or underscore only.";

        // Password: 6+ chars, must have a letter AND a digit
        if (password == null || password.length() < 6)
            return "Password must be at least 6 characters.";
        if (!HAS_LETTER.matcher(password).matches())
            return "Password must contain at least one letter.";
        if (!HAS_DIGIT.matcher(password).matches())
            return "Password must contain at least one digit.";

        // Display name: not empty
        if (displayName == null || displayName.isBlank())
            return "Display name cannot be empty.";

        return null; // null = all valid
    }


    // ═══════════════════════════════════════════════════════════════════════════
    // 4. SEARCH  —  Java Stream filter + Comparator ranking
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Searches all states for the given keyword using Java Streams.
     * Results are sorted by relevance score (highest first).
     */
    public List<State> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return List.of();
        String kw = keyword.toLowerCase().trim();

        return stateMap.values().stream()
                .filter(s -> matchesAny(s, kw))
                .sorted(Comparator.comparingInt((State s) -> score(s, kw)).reversed())
                .toList(); // Java 16+ immutable list
    }

    /** Returns true if the keyword appears in any field of the state. */
    private boolean matchesAny(State s, String kw) {
        return s.getName().toLowerCase().contains(kw)
            || s.getTagline().toLowerCase().contains(kw)
            || s.getDescription().toLowerCase().contains(kw)
            || s.getCulture().toLowerCase().contains(kw)
            || s.getFamousPlaces().stream().anyMatch(p -> p.toLowerCase().contains(kw))
            || s.getFamousFood().stream().anyMatch(f -> f.toLowerCase().contains(kw));
    }

    /** Relevance score: name match = 10 pts, tagline = 5, places/food = 3 each, description = 1. */
    private int score(State s, String kw) {
        int score = 0;
        if (s.getName().toLowerCase().contains(kw))    score += 10;
        if (s.getTagline().toLowerCase().contains(kw)) score += 5;
        if (s.getCulture().toLowerCase().contains(kw)) score += 4;
        score += (int) s.getFamousPlaces().stream().filter(p -> p.toLowerCase().contains(kw)).count() * 3;
        score += (int) s.getFamousFood().stream().filter(f -> f.toLowerCase().contains(kw)).count() * 3;
        if (s.getDescription().toLowerCase().contains(kw)) score += 1;
        return score;
    }


    // ═══════════════════════════════════════════════════════════════════════════
    // 5. VISIT COUNTER  —  ConcurrentHashMap + AtomicInteger (thread-safe)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Thread-safe visit counter per state.
     * ConcurrentHashMap: safe for concurrent access (unlike HashMap).
     * AtomicInteger: lock-free thread-safe integer increment.
     */
    private final ConcurrentHashMap<String, AtomicInteger> visitCounts = new ConcurrentHashMap<>();

    /**
     * Atomically increments the visit count for a state.
     * Returns the new count after increment.
     */
    public int incrementVisit(String stateId) {
        return visitCounts
                .computeIfAbsent(stateId.toLowerCase(), k -> new AtomicInteger(0))
                .incrementAndGet();
    }

    /** Returns visit count for a state. */
    public int getVisitCount(String stateId) {
        AtomicInteger c = visitCounts.get(stateId.toLowerCase());
        return c != null ? c.get() : 0;
    }

    /** Returns total visits across all states. */
    public int getTotalVisits() {
        return visitCounts.values().stream().mapToInt(AtomicInteger::get).sum();
    }

    /**
     * Returns top-N most-visited states as List<Object[]> where:
     *   [0] = State object
     *   [1] = Integer visit count
     */
    public List<Object[]> getTopStates(int n) {
        return stateMap.values().stream()
                .map(s -> new Object[]{ s, getVisitCount(s.getId()) })
                .sorted(Comparator.comparingInt((Object[] arr) -> (int) arr[1]).reversed())
                .limit(n)
                .toList();
    }

    /** Returns all visit counts as Map<stateName, count> sorted by count descending. */
    public Map<String, Integer> getAllCounts() {
        Map<String, Integer> result = new LinkedHashMap<>();
        stateMap.values().stream()
            .sorted(Comparator.comparingInt((State s) -> getVisitCount(s.getId())).reversed())
            .forEach(s -> result.put(s.getName(), getVisitCount(s.getId())));
        return result;
    }


    // ═══════════════════════════════════════════════════════════════════════════
    // CONSTRUCTOR — wires everything together
    // ═══════════════════════════════════════════════════════════════════════════

    public TourService() {
        initializeStates();
        // Pre-seed one demo user: admin / admin123
        users.put("admin", new String[]{ sha256("admin123"), "Admin User" });
    }
}
