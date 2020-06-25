# Molestone
A Java library to parse the Lodestone, the official FFXIV database available online.

# Usage
Get it from maven:
```xml
        <dependency>
            <groupId>com.melodysmaps</groupId>
            <artifactId>Molestone</artifactId>
            <version>1.5.1</version>
        </dependency>
```

# Item
```java
Item i = Item.get(new Lid("f88470aaa49"));
System.out.println(i.toString());
```
Will output:
```
Vintage Hora                             i  48 r  45  f88470aaa49 https://img.finalfa...
```
Item class has all the characteristics you can see on the Lodestone. Get methods should be fairly self-explanatory.

# Duty
```java
Duty r = Duty.get(new Lid("390fb10fd68"));
System.out.println(r.toString());
```
Will output:
```
[RAI] The Ridorana Lighthouse (Regular) @ 390fb10fd68
```
Duty is subclassed into Dungeon, MaplessOneEncounterDuty (i.e. Trial or Ultimate Raid), Raid, Guildhest and PvP. Get methods should be fairly self-explanatory.

# Quest
```java
Quest q = Quest.get(new Lid("40020a8a0b5"));
System.out.println(q.toString());
```
Will output:
```
A Fiendish Likeness @40020a8a0b5 in The Rising Stones (lvl 60) [Heavensward Primal Quests]
```
Get methods should be fairly self-explanatory.

# Recipe
```java
Recipe r = Recipe.get(new Lid("d9b37e6778d"));
System.out.println(r);
```
Will output:
```
Chimerical Felt Hose of Striking (Weaver Lv.59) mats:[3x4d50a7f26d7, 1xad08e38057b, 1xca2fdc94b84, 2x8d869bfbc9b] + crys:[5xb21a951916e, 5x5db117cd77b]
```
Get methods should be fairly self-explanatory.

# Achievement
```java
Achievement a = Achievement.get(new Lid("0274648531b"));
System.out.println(a);
```
Will output:
```
Achievement 'Mastering Magic III' @ 0274648531b (20 points)
```
Get methods should be fairly self-explanatory.

# Shop
```java
Shop s = Shop.get(new Lid("0c3cddbe02f"));
System.out.println(s);
```
Will output:
```
[SHP] Gerulf @ 0c3cddbe02f in Limsa Lominsa, sells for gil
```
Get methods should be fairly self-explanatory.

# Gathering Log
```java
GatheringLogEntry gle = GatheringLogEntry.get(new Lid("15ce02cc550"));
System.out.println(gle.toString());
```
Will output:
```
15ce02cc550 | Gathering lv.1 82a24de366d (Mining/Stone) in [Lv. 5 Hammerlea (Western Thanalan - Thanalan), Lv. 5 Spineless Basin (Central Thanalan - Thanalan)]
```
Get methods should be fairly self-explanatory.
