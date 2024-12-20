
# cart client
man we on duh comethazine 😈

1.21.1 closet cheat 😍 

 


# Cart Client Documentation

---

## Event Handling

### Registering Events
To handle events in your modules, you need to channel your inner wizard and implement event listener interfaces. Here’s how you summon the magic:

1. **Implement the Listener Interface**:
   Each module must pledge allegiance to the event listener interfaces it desires (e.g., `TickListener`, `ItemUseListener`).

    ```java
    public final class AutoCrystal extends Module implements TickListener, ItemUseListener {
        @Override
        public void onTick() {
            // Your sorcery here
        }

        @Override
        public void onItemUse(ItemUseEvent event) {
            // Your wizardry here
        }
    }
    ```

2. **Register and Unregister Events**:
   Use the sacred `EventBus` to register or unregister your listeners when the module springs to life or drifts into the void.

    ```java
    @Override
    public void onEnable() {
        this.eventBus.registerPriorityListener(TickListener.class, this);
        this.eventBus.registerPriorityListener(ItemUseListener.class, this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventBus.unregister(TickListener.class, this);
        this.eventBus.unregister(ItemUseListener.class, this);
        super.onDisable();
    }
    ```

---

## Settings Management

Modules in Cart Client can have settings so configurable, they’ll make even the most finicky user smile. Here’s how to sprinkle in the magic:

### Setting Types

1. **BooleanSetting**:
   A simple toggle for the indecisive.
    ```java
    private final BooleanSetting exampleSetting = new BooleanSetting("Example Setting", false);
    ```

2. **NumberSetting**:
   For numeric input with minimum and maximum limits. No rogue numbers here!
    ```java
    private final NumberSetting placeDelay = new NumberSetting("Place Delay", 0.0, 20.0, 0.0, 1.0);
    ```

3. **StringSetting**:
   For when your module needs words, not numbers.
    ```java
    private final StringSetting exampleString = new StringSetting("Example String", "Default Value");
    ```

### Adding Settings to Modules

Unleash these settings into your module’s domain with the `addSettings` method:

```java
this.addSettings(new Setting[]{
    placeDelay, breakDelay, stopOnKill
});
```

### Accessing Settings

- **Get Setting Value**:
    ```java
    boolean stopOnKillValue = stopOnKill.getValue();
    ```

- **Set Setting Value**:
    ```java
    placeDelay.setValue(10.0);
    ```

---

## Module Creation

Creating a module is like birthing a star—extend the `Module` class and shine brightly.

### Example Module

Here’s a module example so glorious it could make angels weep:

```java
package com.cart.client.module.modules.combat;

import com.cart.client.module.Category;
import com.cart.client.module.Module;
import com.cart.client.event.listeners.TickListener;

public final class ExampleModule extends Module implements TickListener {
    public ExampleModule() {
        super("Example Module", "An example module", 0, Category.COMBAT);
    }

    @Override
    public void onEnable() {
        this.eventBus.registerPriorityListener(TickListener.class, this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventBus.unregister(TickListener.class, this);
        super.onDisable();
    }

    @Override
    public void onTick() {
        // Your tick magic here
    }
}
```

### Key Features of the Example

1. **Constructor**:
   The module is forged with a name, description, keybinding, and category.

2. **Event Registration**:
   `onEnable` and `onDisable` keep things tidy with event registration and unregistration.

3. **Event Handling**:
   `onTick` is where the real party happens.

---

## Utilities

Cart Client is packed with utility classes to make coding less painful and more fun:

- **BlockUtil**:
  Check for block types faster than you can say "obsidian."

- **TargetUtil**:
  Targeting and entity interaction with laser precision.

- **RandomUtil**:
  Because randomness is the spice of life.

- **Mouse**:
  Simulate mouse inputs without actually lifting a finger (okay, maybe a few).

### Example Utility Usage

```java
if (BlockUtil.isBlockType(blockPos, Blocks.OBSIDIAN)) {
    TargetUtil.placeBlock(hitResult, true);
}

int chance = RandomUtil.getRandom(1, 100);
if (chance <= 50) {
    Mouse.pressKeyDefaultDelay(1);
}
```

---

## ModuleManager

Behold, the `ModuleManager`—the maestro conducting all module symphonies.

### Adding Modules

Add your masterpieces to the manager with the `addModules` method:

```java
public void addModules() {
    add(new ExampleModule());
    add(new AutoCrystal());
    add(new DoubleAnchor());
}
```

### Accessing Modules

- **Get All Modules**:
    ```java
    List<Module> modules = moduleManager.getModules();
    ```

- **Get Enabled Modules**:
    ```java
    List<Module> enabledModules = moduleManager.getEnabledModules();
    ```

- **Get Modules by Category**:
    ```java
    List<Module> combatModules = moduleManager.getModulesInCategory(Category.COMBAT);
    ```

---

This documentation should help you boy.

