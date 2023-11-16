# Network Leveling System

My approach for implementing a rigorous levelling system, from initial design to implementation for large player bases. 

## Levelling Progressions

### 1. Linear Levelling Progression

- **Description:**
  - The required XP increases linearly with the player's level.
  - Example: 2500, 5000, 7500, etc.
  - Customizable: Base increase of 3500*n.
  - $XP_{needed} = level \times XP_{perLevel}$

### 2. Exponential Levelling Progression

- **Description:**
  - The required XP increases exponentially with the player's level.
  - Common in MMOS/RPGs like WoW.
  - $XP_{needed} = XP_{base} \times growthRate^{level-1}$


### 3. Polynomial Levelling Progression

- **Description:**
  - The required XP increases according to a power of the player's level.
  - Similar to games like FFXIV, POE, Diablo 3, Skyrim, Oblivion.
  - $XP_{needed} = XP_{base} \times level^{exponent}$
  

### Boosters

- **Description:**
  - Boosters are implemented to enhance XP gain for players.
  - Customizable: Boost percentages, duration, etc.

## Discord Integration

- **Description:**
  - Discord integration for level updates by proxy.

## Notifiers for Best Player Experience

- **Description:**
  - System includes notifiers to enhance the overall player experience.

## Psychology Utilization

- **Description:**
  - Utilizes researched psychological principles to optimize player engagement.

## Customization

- **Description:**
  - The system allows for easy customization of XP progression via config.
