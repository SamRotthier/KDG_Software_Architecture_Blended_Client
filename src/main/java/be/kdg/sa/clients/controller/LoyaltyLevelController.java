package be.kdg.sa.clients.controller;

import be.kdg.sa.clients.controller.dto.LoyaltyLevelDto;
import be.kdg.sa.clients.domain.LoyaltyLevel;
import be.kdg.sa.clients.services.LoyaltyLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loyaltylevels")
public class LoyaltyLevelController {
    private final LoyaltyLevelService loyaltyLevelService;

    @Autowired
    public LoyaltyLevelController(LoyaltyLevelService loyaltyLevelService) {
        this.loyaltyLevelService = loyaltyLevelService;
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping
    public ResponseEntity<List<LoyaltyLevel>> getAllLevels() {
        List<LoyaltyLevel> levels = loyaltyLevelService.getAllLevels();
        return new ResponseEntity<>(levels, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/{id}")
    public ResponseEntity<LoyaltyLevel> getLevelById(@PathVariable Long id) {
        LoyaltyLevel level = loyaltyLevelService.getLevelById(id);
        if(level != null){
            return new ResponseEntity<>(level, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping
    public ResponseEntity<LoyaltyLevel> createLoyaltyLevel(@RequestBody LoyaltyLevelDto loyaltyLevelDto) {
        LoyaltyLevel newLevel = new LoyaltyLevel(
                loyaltyLevelDto.getName(),
                loyaltyLevelDto.getDiscount(),
                loyaltyLevelDto.getMinPoints(),
                loyaltyLevelDto.getMaxPoints()
        );
        LoyaltyLevel savedLevel = loyaltyLevelService.addLevel(newLevel);
        return new ResponseEntity<>(savedLevel, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PutMapping("/{id}")
    public ResponseEntity<LoyaltyLevel> updateLoyaltyLevel(@PathVariable Long id, @RequestBody LoyaltyLevelDto loyaltyLevelDto) {
        LoyaltyLevel existingLevel = loyaltyLevelService.getLevelById(id);
        if (existingLevel != null) {
            existingLevel.setName(loyaltyLevelDto.getName());
            existingLevel.setDiscount(loyaltyLevelDto.getDiscount());
            existingLevel.setMinPoints(loyaltyLevelDto.getMinPoints());
            existingLevel.setMaxPoints(loyaltyLevelDto.getMaxPoints());
            LoyaltyLevel updatedLevel = loyaltyLevelService.updateLevel(existingLevel);
            return new ResponseEntity<>(updatedLevel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/discount")
    public ResponseEntity<Double> getDiscount(@RequestParam int points) {
        double discount = loyaltyLevelService.getDiscount(points);
        return new ResponseEntity<>(discount, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/level")
    public ResponseEntity<LoyaltyLevel> getLevel(@RequestParam int points) {
        LoyaltyLevel level = loyaltyLevelService.getLoyaltyLevel(points);
        return new ResponseEntity<>(level, HttpStatus.OK);
    }
}
