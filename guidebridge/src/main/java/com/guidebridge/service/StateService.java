package com.Easetour.service;

import com.Easetour.model.State;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * StateService - Service Layer
 * Contains all tourism data for each Indian state.
 * Acts as the "database" for this PBL project.
 */
@Service
public class StateService {

    // Ordered map of state id → State object
    private final Map<String, State> stateMap = new LinkedHashMap<>();

    public StateService() {
        initializeStates();
    }

    /**
     * Initializes all 6 state tourism records.
     * In a production app, this data would come from a real database.
     */
    private void initializeStates() {

        // ── GOA ──────────────────────────────────────────────────────────────────
        stateMap.put("goa", new State(
            "goa",
            "Goa",
            "The Pearl of the Arabian Sea",
            "Goa, located on the western coast of India along the Arabian Sea, is famous for its " +
            "beautiful beaches, vibrant nightlife, Portuguese heritage, and scenic landscapes. From " +
            "the lively shores of Baga and Calangute to the peaceful charm of Palolem and Colva, " +
            "Goa offers a perfect mix of relaxation, adventure, culture, and delicious seafood. " +
            "Its historic churches, forts, and colorful festivals make it one of India's most loved tourist destinations.",
            "https://janakitours.com/wp-content/uploads/2025/01/Goa.png",
            "November to February (Pleasant Winter Season)",
            Arrays.asList("Baga Beach", "Calangute Beach", "Anjuna Beach", "Palolem Beach",
                          "Basilica of Bom Jesus", "Fort Aguada", "Dudhsagar Waterfalls"),
            Arrays.asList("Goan Fish Curry", "Prawn Balchão", "Vindaloo", "Bebinca", "Sorpotel", "Feni"),
            "Portuguese heritage, Carnival Festival, Beach festivals, Seafood cuisine, Music and nightlife traditions",
            "Beach hopping, Water sports (parasailing, jet ski, scuba diving), Cruise ride on Mandovi River, Visit churches and forts, Explore flea markets, Enjoy nightlife.",
            "GOA Tourism"
        ));

        // ── KERALA ───────────────────────────────────────────────────────────────
        stateMap.put("kerala", new State(
            "kerala",
            "Kerala",
            "God's Own Country",
            "Kerala, nestled between the Western Ghats and the Arabian Sea, is celebrated for its " +
            "serene backwaters, lush tea and spice plantations, rich cultural traditions, and pristine " +
            "beaches. From the tranquil houseboats of Alleppey to the misty hills of Munnar and the " +
            "wildlife sanctuaries of Periyar, Kerala is a destination that soothes the soul and " +
            "captivates the senses. Ayurvedic wellness, Kathakali dance, and fresh coconut cuisine complete the experience.",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Backwaters_kerala.jpg/1200px-Backwaters_kerala.jpg",
            "October to March (Cool and Pleasant)",
            Arrays.asList("Alleppey Backwaters", "Munnar Tea Gardens", "Kovalam Beach",
                          "Periyar Wildlife Sanctuary", "Thrissur Pooram", "Wayanad Hills", "Fort Kochi"),
            Arrays.asList("Kerala Fish Curry", "Appam with Stew", "Puttu and Kadala", "Sadya", "Karimeen Pollichathu", "Banana Chips"),
            "Kathakali dance, Theyyam rituals, Onam festival, Ayurvedic traditions, Boat races",
            "Houseboat rides in Alleppey, Trekking in Munnar, Wildlife safari in Periyar, Ayurvedic spa treatments, Kathakali performances, Spice plantation tours.",
            "KERALA Tourism"
        ));

        // ── PUNJAB ───────────────────────────────────────────────────────────────
        stateMap.put("punjab", new State(
            "punjab",
            "Punjab",
            "The Land of Five Rivers",
            "Punjab, the breadbasket of India, is a land of vibrant culture, rich history, and warm " +
            "hospitality. Home to the sacred Golden Temple in Amritsar, colorful Bhangra dances, and " +
            "mouth-watering cuisine, Punjab is a state that celebrates life with unmatched energy. " +
            "From the moving ceremony at Wagah Border to the historic battlefields of Panipat and " +
            "the serene Anandpur Sahib, every corner of Punjab tells a story of valor and devotion.",
            "https://upload.wikimedia.org/wikipedia/commons/9/9e/Golden_Temple_reflected_in_the_holy_sarovar.jpg",
            "October to March (Cool and Comfortable)",
            Arrays.asList("Golden Temple Amritsar", "Wagah Border", "Jallianwala Bagh",
                          "Anandpur Sahib", "Rock Garden Chandigarh", "Qila Mubarak Patiala", "Durgiana Temple"),
            Arrays.asList("Butter Chicken", "Dal Makhani", "Sarson da Saag with Makki Roti", "Lassi", "Amritsari Kulcha", "Pinni"),
            "Bhangra dance, Gidda, Lohri & Baisakhi festivals, Sikh heritage, Dhabas culture",
            "Visit the Golden Temple at dawn, Witness the Wagah Border ceremony, Attend Baisakhi celebrations, Try authentic Punjabi dhaba food, Explore Rock Garden.",
            "PUNJAB Tourism"
        ));

        // ── RAJASTHAN ────────────────────────────────────────────────────────────
        stateMap.put("rajasthan", new State(
            "rajasthan",
            "Rajasthan",
            "The Land of Kings",
            "Rajasthan, India's largest state, is a majestic tapestry of golden deserts, towering " +
            "forts, ornate palaces, and vibrant folk traditions. The Pink City of Jaipur dazzles with " +
            "its Amber Fort and Hawa Mahal; Jodhpur's blue-washed lanes and Mehrangarh Fort are " +
            "breathtaking; and the romantic city of Udaipur, set around shimmering lakes, enchants all " +
            "who visit. Camel safaris in the Thar Desert and village havelis complete this royal journey.",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/3/34/Amber_Fort_near_Jaipur%2C_Rajasthan%2C_India.jpg/1200px-Amber_Fort_near_Jaipur%2C_Rajasthan%2C_India.jpg",
            "October to March (Cool Desert Winters)",
            Arrays.asList("Amber Fort Jaipur", "Mehrangarh Fort Jodhpur", "City Palace Udaipur",
                          "Jaisalmer Fort", "Hawa Mahal", "Ranthambore National Park", "Thar Desert"),
            Arrays.asList("Dal Baati Churma", "Laal Maas", "Ghevar", "Ker Sangri", "Mirchi Bada", "Mawa Kachori"),
            "Rajput heritage, Pushkar Camel Fair, Teej & Gangaur festivals, folk music (Manganiyar), puppet shows",
            "Camel safari in Thar Desert, Explore Amber Fort by elephant, Hot air balloon over Jaipur, Village heritage walk, Watch folk dance performances, Sunrise over Jaisalmer Fort.",
            "RAJASTHAN Tourism"
        ));

        // ── UTTAR PRADESH ────────────────────────────────────────────────────────
        stateMap.put("uttar-pradesh", new State(
            "uttar-pradesh",
            "Uttar Pradesh",
            "The Heartland of India",
            "Uttar Pradesh is India's most populous state and one of its most historically significant. " +
            "Home to the iconic Taj Mahal in Agra — a wonder of the world — the spiritual ghats of " +
            "Varanasi, the birthplace of Lord Krishna in Mathura, and the enlightenment site of Lord Buddha " +
            "in Sarnath, UP is a sacred pilgrimage for millions. Lucknow's Nawabi architecture, " +
            "fragrant biryanis, and chikan embroidery add a cultural richness to this incredible state.",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/da/Taj-Mahal.jpg/1200px-Taj-Mahal.jpg",
            "October to March (Best Weather)",
            Arrays.asList("Taj Mahal Agra", "Varanasi Ghats", "Mathura & Vrindavan",
                          "Lucknow Bara Imambara", "Sarnath", "Fatehpur Sikri", "Agra Fort"),
            Arrays.asList("Lucknowi Biryani", "Tunday Kababi", "Peda from Mathura", "Chaat from Varanasi", "Bedmi Puri", "Shahi Tukda"),
            "Nawabi culture of Lucknow, Dev Deepawali in Varanasi, Holi in Mathura, Buddhist heritage in Sarnath",
            "Sunrise at Taj Mahal, Ganga Aarti in Varanasi, Boat ride on the Ganga, Explore Lucknow's Nawabi lanes, Holi celebrations in Mathura, Visit Fatehpur Sikri.",
            "UTTAR PRADESH Tourism"
        ));

        // ── UTTARAKHAND ──────────────────────────────────────────────────────────
        stateMap.put("uttarakhand", new State(
            "uttarakhand",
            "Uttarakhand",
            "The Land of Gods",
            "Uttarakhand, nestled in the Himalayas, is a state of breathtaking natural beauty and deep " +
            "spiritual significance. Known as 'Devbhoomi' (Land of Gods), it is home to the sacred Char " +
            "Dham pilgrimage sites of Badrinath, Kedarnath, Gangotri, and Yamunotri. The adventure hub of " +
            "Rishikesh offers world-class river rafting and yoga retreats; the Valley of Flowers bursts " +
            "with alpine blooms; and the hill stations of Mussoorie and Nainital enchant year-round.",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/6/69/Kedarnath_Temple.jpg/1200px-Kedarnath_Temple.jpg",
            "April to June & September to November",
            Arrays.asList("Char Dham (Badrinath, Kedarnath, Gangotri, Yamunotri)", "Rishikesh",
                          "Jim Corbett National Park", "Nainital Lake", "Mussoorie", "Valley of Flowers", "Auli Ski Resort"),
            Arrays.asList("Kafuli", "Chainsoo", "Bal Mithai", "Aloo Ke Gutke", "Bhang Ki Chutney", "Mandua Ki Roti"),
            "Hindu pilgrimage traditions, Haridwar Kumbh Mela, Yoga & meditation in Rishikesh, folk dance Chholiya",
            "River rafting in Rishikesh, Char Dham Yatra, Trekking in Valley of Flowers, Skiing in Auli, Ganga Aarti in Haridwar, Wildlife safari in Jim Corbett, Yoga retreat.",
            "UTTARAKHAND Tourism"
        ));
    }

    /**
     * Returns all states as a list (for the selection dropdown).
     */
    public List<State> getAllStates() {
        return List.copyOf(stateMap.values());
    }

    /**
     * Returns a single state by its ID slug.
     * @param id e.g. "goa", "kerala"
     * @return Optional<State>
     */
    public Optional<State> getStateById(String id) {
        return Optional.ofNullable(stateMap.get(id.toLowerCase()));
    }

    /**
     * Returns map of state id → info snippet for the Select page AJAX info card.
     */
    public Map<String, String> getStateInfoMap() {
        Map<String, String> infoMap = new LinkedHashMap<>();
        for (State s : stateMap.values()) {
            infoMap.put(s.getId(), "Popular in " + s.getName() + " — " + s.getTagline());
        }
        return infoMap;
    }
}
