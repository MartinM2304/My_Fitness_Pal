# MyFitnessPal - A Clean Code Journey

## Overview

Welcome to **MyFitnessPal**, a console-based fitness tracking application I crafted during the **Clean Code Course** at Sofia University "St. Kliment Ohridski." This project is more than just code—it’s a testament to my growth as a developer, blending practical features with elegant design patterns and clean code practices. With MyFitnessPal, users can log food consumption, track water intake, plan meals, and persist data in JSON, all while I honed my skills under the guidance of Robert C. Martin’s timeless principles.

---

## Features

- **Food Tracking**: Record nutritional details and consumption logs by date and meal time.
- **Water Intake**: Monitor daily water consumption effortlessly.
- **Meal Planning**: Build meals from food items, linked by stable IDs or keys.
- **Data Persistence**: Save and load data using JSON files.
- **Console Interface**: A simple, command-driven UI for fitness management.

---

## Technologies Used

- **Java 17**: My foundation, leveraging modern features for clarity and efficiency.
- **Gson**: A lightweight JSON library for seamless serialization.
- **Maven**: My tool for managing dependencies and builds (adjust if not used).
- **JUnit 5** (optional): For testing robustness (include if applicable).

---

## Design Patterns: My Toolkit

In building MyFitnessPal, I wielded a set of design patterns to ensure the code was as flexible as it was functional:

### Visitor Pattern
- **What I Did**: Separated serialization logic from my `Consumable` classes (`Food`, `Water`, `Meal`) using `Visitable` and `ItemVisitor`. A `JsonItemVisitor` crafts JSON without touching the domain.
- **Why It Matters**: I can extend operations—like adding XML support—without altering core classes, embracing openness while staying closed to modification.

### Strategy Pattern
- **What I Did**: Abstracted serialization into a `SerializationStrategy` interface, with `JsonSerializationStrategy` handling Gson-based JSON.
- **Why It Matters**: Swapping strategies became a breeze, keeping my design adaptable and my dependencies inverted.

### Builder Pattern
- **What I Did**: Used `Food.Builder` to construct `Food` objects with a fluent, readable API.
- **Why It Matters**: Complex object creation turned simple and safe, ensuring immutability and clarity.

### Factory Pattern
- **What I Did**: Implemented a `CommandFactory` to instantiate `Command` objects based on user input (e.g., `AddFoodCommand`).
- **Why It Matters**: Centralizing command creation streamlined my logic, making it easy to add new actions without cluttering the `Controller`.

### Command Pattern
- **What I Did**: Encapsulated user actions as `Command` objects, executed via the factory.
- **Why It Matters**: Decoupled input from execution, keeping my system modular and extensible.

### Dependency Injection
- **What I Did**: Injected `ItemSerializer` and `UserInterface` into `Controller`, and `SerializationStrategy` into `ItemSerializer`.
- **Why It Matters**: Reduced coupling, boosted testability, and honored inversion of control.

---

## Clean Code
When I started the Clean Code Course, I saw code as a tool to get things done. Now, after building MyFitnessPal, I see it as a craft. I embraced **SOLID principles** to shape a system that’s both powerful and elegant:

- I gave each class a **single responsibility**, like letting `ItemSerializer` focus solely on persistence while `Controller` orchestrated the flow. This clarity made debugging a joy instead of a chore.
- I kept my design **open for extension but closed for modification**, using patterns like Visitor and Strategy. Adding a new serialization format? Just plug in a new strategy—no surgery required.
- My `Consumable` hierarchy (`Food`, `Water`, `Meal`) respects **Liskov substitution**, ensuring any subtype can stand in without breaking the system.
- I crafted **narrow interfaces**—like `SerializationStrategy`—so no class bears the burden of unneeded methods, keeping things lean and focused.
- By **depending on abstractions**, not implementations, I inverted control. `Controller` doesn’t care how `ItemSerializer` works—it just trusts the interface.

---

