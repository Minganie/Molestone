# Molestone
A Java library to parse the Lodestone, the official FFXIV database available online.

# Item
```java
Item i = Item.get(new Lid("f88470aaa49"));
System.out.println(i.toString());
```
Will output:
```java
Vintage Hora                             i  48 r  45  f88470aaa49 https://img.finalfa...
```
Item class has all the characteristics you can see on the Lodestone. I haven't documented them well (sorry!) but the "get" methods should be fairly self-explanatory.

# Duty
```java
Duty r = Duty.get(new Lid("390fb10fd68"));
System.out.println(r.toString());
```
Will output:
```java
[RAI] The Ridorana Lighthouse (Regular) @ 390fb10fd68
```
Duty is subclassed into Dungeon, Trial, Raid, Guildhest and PvP. Get methods should be fairly self-explanatory.

# Quest
```java
Quest q = Quest.get(new Lid("40020a8a0b5"));
System.out.println(q.toString());
```
Will output:
```java
A Fiendish Likeness @40020a8a0b5 in The Rising Stones (lvl 60) [Heavensward Primal Quests]
```
Get methods should be fairly self-explanatory.

# Recipe
```java
Recipe r = Recipe.get(new Lid("d9b37e6778d"));
System.out.println(r);
```
Will output:
```java
Chimerical Felt Hose of Striking (Weaver Lv.59) mats:[3x4d50a7f26d7, 1xad08e38057b, 1xca2fdc94b84, 2x8d869bfbc9b] + crys:[5xb21a951916e, 5x5db117cd77b]
```
