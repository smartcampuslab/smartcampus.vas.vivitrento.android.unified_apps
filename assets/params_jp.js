/*
 * SHOW ALERTROADS IN LEG MAP (planning)
 * "alertroads_in_planning" : true,
 * "alertroads_in_planning_agencyid" : "COMUNE_DI_ROVERETO",
 * 
 * AGENCY ID
 * 	
 *  TRAIN_BZVR  5;
 *	TRAIN_TNBDG  6;
 *	TRAIN_TM  10;
 *	BUS_TRENTO  12;
 *	BUS_ROVERETO  16;
 *	BUS_SUBURBAN  17;
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
 * SUBURBAN ZONES
 * 1 Val di Fiemme, Val di Fassa, Val di Cembra
 * 2 Val Rendena, Giudicarie
 * 3 Rovereto, Lavarone, Destra Adige, Riva del Garda
 * 4 Valsugana
 * 5 Valle di Primiero
 * 6 Val di Non, Val di Sole
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
 * 
 *     "suburban_zones" : [
 *        3
 *   ],
 */

{
    "app_token" : "viaggiarovereto",
    "alertroads_in_planning" : true,
    "alertroads_in_planning_agencyid" : "COMUNE_DI_ROVERETO",
    "smartcheck_options" : [
        2,
        3,
        4,
        6,
        7
    ],
    "suburban_zones" : [
	    3
    ],
     "agency" : [
                {"agency_id":"5"},
                {"agency_id":"6"},
                {"agency_id":"10"},
                {"agency_id":"16"},
                {"agency_id":"17",
                	"routes_id":[
                	             "110_17_0",
                	             "110_17_1",
                	             "181_17_0",
                	             "181_17_1",
                	             "186_17_0",
                	             "186_17_1",
                	             "190_17_0",
                	             "190_17_1",
                	             "194_17_0",
                	             "194_17_1",
                	             "196_17_0",
                	             "196_17_1",
                	             "198_17_0",
                	             "198_17_1",
                	             "200_17_0",
                	             "200_17_1",
                	             "203_17_0",
                	             "203_17_1",
                	             "205_17_0",
                	             "215_17_0",
                	             "215_17_1",
                	             "220_17_0",
                	             "220_17_1",
                	             "231_17_0",
                	             "231_17_1",
                	             "233_17_0",
                	             "233_17_1",
                	             "235_17_0",
                	             "235_17_1",
                	             "503_17_0",
                	             "507_17_0",
                	             "580_17_0",
                	             "580_17_1",
                	             "634_17_0",
                	             "634_17_1"
                	             ]
                }],
    "broadcast_notifications_options" : [
        2,
        3,
        4
    ],
   "center_map" : [
      45.890919,
      11.040184
   ],
   "zoom_map" : 15
}
