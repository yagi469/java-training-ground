# Java Ironclad Patterns (Snippets)

## Controller Pattern

```java
@RestController
@RequestMapping("/api/v1/resource-name") // TODO: Change path
@RequiredArgsConstructor
public class ResourceController {

    // private final ResourceService service; // TODO: Uncomment when Service is ready

    /**
     * Registration / Creation
     */
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid CreateRequest request) {
        // service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // TODO: Add DTO classes (inner static class or separate file)
    // public record CreateRequest(@NotBlank String name) {}
}
```

### Key Points
1.  **Annotations**: `@RestController`, `@RequestMapping`, `@RequiredArgsConstructor` are the "Holy Trinity". Always write them first.
2.  **Constructor Injection**: `@RequiredArgsConstructor` (Lombok) handles dependency injection automatically for `final` fields.
3.  **Input Validation**: Always use `@RequestBody` and `@Valid` together for POST/PUT.
4.  **Response**: Return `ResponseEntity<T>`. Use `Void` if no body is returned.
