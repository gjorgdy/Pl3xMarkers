![An area and a Nether Portal marker on a webmap](https://cdn.modrinth.com/data/cached_images/1a17539794c717c310b9bf3e3dd8f0e4d4e9070a_0.webp)

<center>
  Easily show all your points of interest on your server's <a href="https://modrinth.com/plugin/pl3xmap">Pl3xMap</a> without running a single command.
</center>

<br>

## Features

- 'Usable' Portals through tooltips
- Nether Portal Markers
- End Portal Markers
- End Gateway Markers
- Beacon Markers
- Player made areas


## How to -

### Make an area

1. Get a lodestone and a banner (all the same color) for all corners of your area.
2. Give all banners the name of your area in an anvil.
3. Place a lodestone with one of the banners on top on every corner.
4. Admire your area on the web map.

- Corners/points can be max 128 blocks apart.
- The names on the banners need to be an exact match.
- The placement order does not matter, the points will be connected automatically.
- You can add or remove points as you wish, the area will adept.
- For the best results, use at least 4 points.

### Add a portal marker
1. Go through a portal.
2. Admire your portal on the web map.

### Add a beacon marker
1. Place a beacon.
2. Activate the beacon.
3. Admire your b~~e~~acon on the web map.

## FAQ
### Q: The layers are not showing up on my web map, what do I do?
A: You most likely use custom names for your worlds. Make sure they use the correct suffix structure: ``{worldname}``, ``{worldname}_nether``, ``{worldname}_the_end``
### Q: How to make my areas show up?
A: The algorithm used for the areas ignores points that are too far inwards. Make sure your points are placed on the corners of your area and not too close to each other.
### Q: Can I customize area labels?
A: Yes! Area names support basic HTML tags such as `<b>`, `<i>`, `<u>`, `<br>`, and `<span style="color: #RRGGBB;"> mooi ding </span>`.

## Demo

The Pl3xMarkers mod started out as Helix Markers, for my server; [Helix Survival](https://play.hexasis.eu/)

You can check its webmap at [https://map.hexasis.eu/](https://map.hexasis.eu/?world=minecraft-overworld&renderer=vintage_story&zoom=2&x=842&z=1412)

## !! Warning !!

Pl3xMarkers is still an experimental mod, while there are no risks to your world by using it. You might have to reset your markers when updating the mod.

For portals and beacons this just means they should be used and activated. Where for areas you need to replace the banners to re-register the points.
