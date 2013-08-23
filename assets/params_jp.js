/*
 * SHOW ALERTROADS IN LEG MAP (planning)
 * "alertroads_in_planning" : true,
 * 
 * SMART CHECK OPTIONS
 * 1 bus trento timetable
 * 2 bus rovereto timetable
 * 3 suburban timetable
 * 4 train timetable
 * 5 parking trento
 * 6 parking rovereto
 * 7 alerts rovereto
 * 
 * BROADCAST NOTIFICATIONS OPTIONS
 * 1 bus trento delay
 * 2 bus rovereto delay
 * 3 bus suburban delay
 * 4 train delay
 * 5 accident
 * 6 road works
 * 7 strike
 * 8 traffic jam
 * 9 diversion
 * 
 * COORDINATES
 * Trento 46.069672, 11.121270
 * Rovereto 45.890919, 11.040184
 */

{
    "app_token" : "viaggiarovereto",
    "alertroads_in_planning" : true,
    "smartcheck_options" : [
        2,
        3,
        4,
        6,
        7
    ],
    "broadcast_notifications_options" : [
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9
    ],
   "center_map" : [
      45.890919,
      11.040184
   ],
   "zoom_map" : 15
}