package com.guidebridge.controller;

import com.guidebridge.model.State;
import com.guidebridge.service.TourService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * AppController — Spring MVC Controller
 *
 * Handles all HTTP routes. Delegates all logic to TourService.
 *
 * Routes:
 *   GET  /              → login page
 *   POST /login         → authenticate via TourService (SHA-256)
 *   GET  /register      → register page
 *   POST /register      → register via TourService (regex validation)
 *   GET  /logout        → invalidate session
 *   GET  /select        → state selection  [session protected]
 *   GET  /state/{id}    → state detail     [session protected]
 *   GET  /state/{id}/places → places page  [session protected]
 *   GET  /state/{id}/food   → food page    [session protected]
 *   GET  /search        → search results   [session protected]
 *   GET  /stats         → visit stats      [session protected]
 */
@Controller
public class AppController {

    private final TourService tourService;

    // Constructor injection — Spring auto-wires TourService
    public AppController(TourService tourService) {
        this.tourService = tourService;
    }

    // ─── SESSION HELPER ──────────────────────────────────────────────────────────

    /** Returns the logged-in username from the session, or null if not logged in. */
    private String sessionUser(HttpSession session) {
        Object user = session.getAttribute("username");
        return (user instanceof String s) ? s : null; // Java 16 pattern matching
    }

    /** Adds the logged-in user's display name to the Thymeleaf model. */
    private void addUser(HttpSession session, Model model) {
        String username = sessionUser(session);
        if (username != null) {
            model.addAttribute("displayName", tourService.getDisplayName(username));
        }
    }

    // ─── LOGIN ───────────────────────────────────────────────────────────────────

    @GetMapping({"/", "/login"})
    public String showLogin(HttpSession session) {
        if (sessionUser(session) != null) return "redirect:/select";
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam(defaultValue = "") String username,
                          @RequestParam(defaultValue = "") String password,
                          HttpSession session, Model model) {

        if (username.isBlank() || password.isBlank()) {
            model.addAttribute("error", "Please enter both username and password.");
            return "login";
        }

        // TourService.authenticate() does real SHA-256 comparison
        String displayName = tourService.authenticate(username, password);
        if (displayName == null) {
            model.addAttribute("error", "Invalid username or password. Try admin / admin123.");
            return "login";
        }

        session.setAttribute("username", username.toLowerCase().trim());
        session.setMaxInactiveInterval(30 * 60); // 30 min timeout
        return "redirect:/select";
    }

    // ─── REGISTER ────────────────────────────────────────────────────────────────

    @GetMapping("/register")
    public String showRegister(HttpSession session) {
        if (sessionUser(session) != null) return "redirect:/select";
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam(defaultValue = "") String username,
                             @RequestParam(defaultValue = "") String password,
                             @RequestParam(defaultValue = "") String displayName,
                             Model model) {

        // TourService.register() runs regex validation + SHA-256 hash
        String error = tourService.register(username, password, displayName);
        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("username", username);
            model.addAttribute("displayName", displayName);
            return "register";
        }

        model.addAttribute("success", "Account created! You can now log in.");
        return "login";
    }

    // ─── LOGOUT ──────────────────────────────────────────────────────────────────

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ─── STATE SELECTION ─────────────────────────────────────────────────────────

    @GetMapping("/select")
    public String showSelect(HttpSession session, Model model) {
        if (sessionUser(session) == null) return "redirect:/login";
        model.addAttribute("states", tourService.getAllStates());
        addUser(session, model);
        return "select";
    }

    // ─── STATE DETAIL ────────────────────────────────────────────────────────────

    @GetMapping("/state/{id}")
    public String showState(@PathVariable String id, HttpSession session, Model model) {
        if (sessionUser(session) == null) return "redirect:/login";

        Optional<State> stateOpt = tourService.getStateById(id);
        if (stateOpt.isEmpty()) return "redirect:/select";

        // Increment visit count via TourService (ConcurrentHashMap + AtomicInteger)
        int visits = tourService.incrementVisit(id);

        model.addAttribute("state", stateOpt.get());
        model.addAttribute("visitCount", visits);
        addUser(session, model);
        return "state/detail";
    }

    // ─── PLACES SUB-PAGE ─────────────────────────────────────────────────────────

    @GetMapping("/state/{id}/places")
    public String showPlaces(@PathVariable String id, HttpSession session, Model model) {
        if (sessionUser(session) == null) return "redirect:/login";

        Optional<State> stateOpt = tourService.getStateById(id);
        if (stateOpt.isEmpty()) return "redirect:/select";

        model.addAttribute("state", stateOpt.get());
        addUser(session, model);
        return "state/places";
    }

    // ─── FOOD SUB-PAGE ───────────────────────────────────────────────────────────

    @GetMapping("/state/{id}/food")
    public String showFood(@PathVariable String id, HttpSession session, Model model) {
        if (sessionUser(session) == null) return "redirect:/login";

        Optional<State> stateOpt = tourService.getStateById(id);
        if (stateOpt.isEmpty()) return "redirect:/select";

        model.addAttribute("state", stateOpt.get());
        addUser(session, model);
        return "state/food";
    }

    // ─── SEARCH ──────────────────────────────────────────────────────────────────

    @GetMapping("/search")
    public String search(@RequestParam(name = "q", defaultValue = "") String query,
                         HttpSession session, Model model) {
        if (sessionUser(session) == null) return "redirect:/login";

        // TourService.search() uses Java Stream.filter() + Comparator ranking
        List<State> results = tourService.search(query);
        model.addAttribute("results", results);
        model.addAttribute("query", query);
        model.addAttribute("resultCount", results.size());
        addUser(session, model);
        return "search";
    }

    // ─── STATS ───────────────────────────────────────────────────────────────────

    @GetMapping("/stats")
    public String showStats(HttpSession session, Model model) {
        if (sessionUser(session) == null) return "redirect:/login";

        // TourService.getTopStates() uses Stream + ConcurrentHashMap data
        model.addAttribute("topStates", tourService.getTopStates(6));
        model.addAttribute("allCounts", tourService.getAllCounts());
        model.addAttribute("totalVisits", tourService.getTotalVisits());
        model.addAttribute("userCount", tourService.getUserCount());
        addUser(session, model);
        return "stats";
    }
}
