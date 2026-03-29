package com.Easetour.controller;

import com.Easetour.model.State;
import com.Easetour.service.StateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * AppController - Spring MVC Controller
 *
 * Handles all HTTP routes for the Easetour tourist guide application.
 *
 * Routes:
 *   GET  /             → Login page
 *   GET  /login        → Login page (alias)
 *   POST /login        → Process login → redirect to /select
 *   GET  /select       → State selection page
 *   GET  /state/{id}   → State detail page
 *   GET  /state/{id}/places → Famous places sub-page
 *   GET  /state/{id}/food   → Famous food sub-page
 */
@Controller
public class AppController {

    private final StateService stateService;

    // Constructor injection (best practice over @Autowired field injection)
    public AppController(StateService stateService) {
        this.stateService = stateService;
    }

    // ─── LOGIN ───────────────────────────────────────────────────────────────────

    /**
     * GET / and GET /login → render the login page
     */
    @GetMapping({"/", "/login"})
    public String showLoginPage() {
        return "login";  // resolves to templates/login.html
    }

    /**
     * POST /login → validate and redirect to state selection
     * For PBL demo: any non-empty username + password is accepted
     */
    @PostMapping("/login")
    public String processLogin(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password,
            Model model) {

        // Basic validation: fields must not be empty
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            model.addAttribute("error", "Please enter both username and password.");
            return "login";
        }

        // Accept any credentials (open login for PBL demo)
        return "redirect:/select";
    }

    // ─── STATE SELECTION ─────────────────────────────────────────────────────────

    /**
     * GET /select → Show state selection dropdown
     * Passes all states to the Thymeleaf template
     */
    @GetMapping("/select")
    public String showSelectPage(Model model) {
        model.addAttribute("states", stateService.getAllStates());
        return "select";  // resolves to templates/select.html
    }

    // ─── STATE DETAIL ────────────────────────────────────────────────────────────

    /**
     * GET /state/{id} → Show full detail page for a specific state
     * e.g. /state/goa, /state/kerala, /state/rajasthan
     */
    @GetMapping("/state/{id}")
    public String showStatePage(@PathVariable String id, Model model) {
        Optional<State> stateOpt = stateService.getStateById(id);

        if (stateOpt.isEmpty()) {
            // If state not found, redirect back to selection
            return "redirect:/select";
        }

        model.addAttribute("state", stateOpt.get());
        return "state/detail";  // resolves to templates/state/detail.html
    }

    // ─── PLACES SUB-PAGE ─────────────────────────────────────────────────────────

    /**
     * GET /state/{id}/places → Famous places sub-page for a state
     */
    @GetMapping("/state/{id}/places")
    public String showPlacesPage(@PathVariable String id, Model model) {
        Optional<State> stateOpt = stateService.getStateById(id);

        if (stateOpt.isEmpty()) {
            return "redirect:/select";
        }

        model.addAttribute("state", stateOpt.get());
        return "state/places";  // resolves to templates/state/places.html
    }

    // ─── FOOD SUB-PAGE ───────────────────────────────────────────────────────────

    /**
     * GET /state/{id}/food → Famous food sub-page for a state
     */
    @GetMapping("/state/{id}/food")
    public String showFoodPage(@PathVariable String id, Model model) {
        Optional<State> stateOpt = stateService.getStateById(id);

        if (stateOpt.isEmpty()) {
            return "redirect:/select";
        }

        model.addAttribute("state", stateOpt.get());
        return "state/food";  // resolves to templates/state/food.html
    }
}
