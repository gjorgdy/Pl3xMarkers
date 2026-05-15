Fixes and Automatic Migration
---

- Fixed an issue where disabling areas would crash the server on a player joining.
- Added automatic migration; if you are coming from a version between 0.5.0 and 0.7.0, all markers will automatically be
  converted to the new storage system.
    - The old system did not store the Y-coordinate of markers, passive markers as Nether Portals and Beacons will
      update these automatically, but things like areas and signs have to be edited to update.
    - This migration will be removed in the 1.0 release, so if you are still on a version before 0.7.0, please update as
      soon as possible to avoid losing data.