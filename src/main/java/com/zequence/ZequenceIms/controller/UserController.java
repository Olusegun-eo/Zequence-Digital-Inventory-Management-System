package com.zequence.ZequenceIms.controller;


import com.zequence.ZequenceIms.RequireAuthentication;
import com.zequence.ZequenceIms.dto.ApiErrorResponse;
import com.zequence.ZequenceIms.dtos.UserUpdateRequestDTO;
import com.zequence.ZequenceIms.dtos.UserUpdateResponseDTO;
import com.zequence.ZequenceIms.entity.MessageResponse;
import com.zequence.ZequenceIms.entity.UserAuthenticationDetails;
import com.zequence.ZequenceIms.exceptions.InvalidIdException;
import com.zequence.ZequenceIms.exceptions.UserNotFoundException;
import com.zequence.ZequenceIms.helper.JwtHelper;
import com.zequence.ZequenceIms.service.userService.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;



@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtHelper jwtHelper;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    //@PreAuthorize("hasRole('USER')")
//    @RequireAuthentication
//    @PutMapping(value = "/update/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> updateUserDetails(@PathVariable Long userId,
//                                               @RequestPart("userUpdateRequest") @Valid UserUpdateRequestDTO userUpdateRequest,
//                                               @RequestPart(value = "companyLogo", required = false) MultipartFile companyLogo,
//                                               @RequestHeader("Authorization") String authHeader) {
//
//        logger.info("Received update request for user ID: {}", userId);
//        logger.info("Authorization header: {}", authHeader);
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            logger.error("Invalid token: {}", authHeader);
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid token"));
//        }
//
//        String token = authHeader.substring(7);
//        logger.info("Extracted token: {}", token);
//
//        String authenticatedUsername = jwtHelper.extractUsername(token);
//        logger.info("Authenticated username: {}", authenticatedUsername);
//
//        try {
//            UserAuthenticationDetails userAuthDetails = userService.getUserAuthDetailsById(userId);
//            if (!authenticatedUsername.equals(userAuthDetails.getUsername()) && !authenticatedUsername.equals(userAuthDetails.getEmail())) {
//                logger.error("Unauthorized data modification attempt by user: {}", authenticatedUsername);
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Unauthorized to update user details"));
//            }
//
//            logger.info("User update request: {}", userUpdateRequest);
//            if (companyLogo != null) {
//                logger.info("Company logo file name: {}", companyLogo.getOriginalFilename());
//            } else {
//                logger.info("No company logo provided");
//            }
//
//            UserUpdateResponseDTO updatedUser = userService.updateUserDetails(userId, userUpdateRequest, companyLogo, token);
//            logger.info("User updated successfully: {}", updatedUser);
//            return ResponseEntity.ok(updatedUser);
//        } catch (UserNotFoundException e) {
//            logger.error("User not found: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
//        } catch (InvalidIdException | ResponseStatusException e) {
//            logger.error("Invalid ID or response status exception: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(e.getMessage()));
//        } catch (RuntimeException e) {
//            logger.error("Unexpected runtime exception occurred: {}", e.getMessage(), e);
//            return ResponseEntity.internalServerError().build();
//        } finally {
//            logger.info("User details update process completed");
//        }
//    }

    @RequireAuthentication
    @PutMapping(value = "/update/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserDetails(@PathVariable Long userId,
                                               @RequestPart("userUpdateRequest") @Valid UserUpdateRequestDTO userUpdateRequest,
                                               @RequestPart(value = "companyLogo", required = false) MultipartFile companyLogo,
                                               @RequestHeader("Authorization") String authHeader) {

        logger.info("Received update request for user ID: {}", userId);
        logger.info("Authorization header: {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.error("Invalid token: {}", authHeader);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid token"));
        }

        String token = authHeader.substring(7);
        logger.info("Extracted token: {}", token);

        String authenticatedUsername = jwtHelper.extractUsername(token);
        logger.info("Authenticated username: {}", authenticatedUsername);

        try {
            UserAuthenticationDetails userAuthDetails = userService.getUserAuthDetailsById(userId);
            if (!authenticatedUsername.equals(userAuthDetails.getUsername()) && !authenticatedUsername.equals(userAuthDetails.getEmail())) {
                logger.error("Unauthorized data modification attempt by user: {}", authenticatedUsername);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Unauthorized to update user details"));
            }

            logger.info("User update request: {}", userUpdateRequest);
            if (companyLogo != null) {
                logger.info("Company logo file name: {}", companyLogo.getOriginalFilename());
            } else {
                logger.info("No company logo provided");
            }

            UserUpdateResponseDTO updatedUser = userService.updateUserDetails(userId, userUpdateRequest, companyLogo, token);
            logger.info("User updated successfully: {}", updatedUser);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        } catch (InvalidIdException | ResponseStatusException e) {
            logger.error("Invalid ID or response status exception: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(e.getMessage()));
        } catch (RuntimeException e) {
            logger.error("Unexpected runtime exception occurred: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        } finally {
            logger.info("User details update process completed");
        }
    }

    @RequireAuthentication
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            String result = userService.logout(token);
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.badRequest().body(new ApiErrorResponse(400, "Invalid token"));
        }
    }
    /*
    @PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/delete/{id}")
	public String deleteUser(
			@RequestParam(name = "error", required = false) String error,
			@RequestParam(name = "success", required = false) String success,
			RedirectAttributes redirectAttributes,
			@PathVariable Long id,
			Principal principal,
			Model model) {

		String loggedInUsername = principal.getName();

		User loggedInUser = userService.findUserByEmail(loggedInUsername);

		if (loggedInUser != null && loggedInUser.getId().equals(id)) {
			if (error != null) {
				redirectAttributes.addFlashAttribute("error", "You cannot delete yourself.");
			}
		} else {
			if (userService.doesUserExist(id)) {
				userService.deleteUserById(id);
				if (success != null) {
					redirectAttributes.addFlashAttribute("success", "User has been deleted successfully");
				}
			} else {
				if (error != null) {
					redirectAttributes.addFlashAttribute("error", "User does not exist");
				}
			}
		}
		return "redirect:/users";
	}
     */
}
