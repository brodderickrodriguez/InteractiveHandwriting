## Recommended Future Work and Bug Fixes 

* Fix virtual room management 
* The menu displays the list of rooms that are available to join. However, a device will still display a room as an option after all devices have left that room. One possible solution is to add reference counting to each room (how many devices occupy the room), and remove the room when references reach zero. 
* Add document transfers between devices 
* Devices can load and position a document (pdf) within a room, but the document is not visible on other devices. This would involve adding document transfers to the network (already possible with Nearby Connections API). 
* Fix document exports 
* The user should be able to save their drawings to a file. That file should be able to be converted to an image. Also, that file should be able to be loaded back into the application to continue working on that specific whiteboard. 
* Resolve issues with compatibility between devices of different sizes
* Android devices come in different shapes and sizes. The edits on one device do not currently scale and position properly across different devices. This would involve linear transforms between local coordinate systems to a global coordinate system and vice versa.  
* Add indicators to canvas
* Some user interactions should appear on portions of canvas not visible on all devices. An arrow or some other indicator should show users where edits are being made off screen.
* Add indicators to buttons 
* When a button like “erase” is selected, the user is not properly informed that they took this action. One solution would be to toggle the color of buttons. 
* Optimize the action synchronization process joining rooms 
* Currently, the devices synchronize rooms by flooding the network with an individual packet for each action they have seen. This can be optimized by packing more than one action into each packet. 
* Fix device screen rotation bugs 
* When the user rotates the device, all markups are erased. This is because the RoomView is constructed again. A solution would be to save the internal state of the RoomView object during a rotation and reload that state into the new RoomView object. 
* Fix closing the application erases progress
* When the application is closed or navigated away, all drawings are lost. One solution would be move the Canvas Manager to a service, so that it exists even when the user navigates away briefly. Another solution would be to save all drawings to a file and to reload those drawings when the user reopens the application. 
* Add a full routing implementation to the Nearby Connections Network Layer
* Each device implements a routing table to manage rooms. This routing table includes next hop, and hop distance. The issue is that each the devices currently use the routing tables to identify rooms. These routing tables can be leveraged in future work in order to build an ad-hoc style network. Instead of devices connecting in a fully-connected graph, the devices could send messages to devices that multiple hops away. 
* Replace the Nearby Connections API with a different API
* The network layer architecture is a flexible, interface-based design. This allows the networking layer to be rebuilt with a different API (such as with Bluetooth only) with no modifications to the application (user facing) layer. 
* Extend the canvas manager 
* The canvas manager’s functionality can be extended to manage drawables for more than one canvas. In effect, an endless canvas can be implemented for the room view by stitching together a patchwork of canvas managed by the canvas manager. The associated coordinates for each draw action would then be a high level coordinate system mapped onto the correct canvas. 
* Add multipage documents viewing
* Currently when a document is rendered on the screen only the first page is shown.  Multi-page scrolling for documents should be added.
* When a pan or zoom occurs within the whiteboard, the next drawings may appear in locations the user did not touch. 
* This is a result of some bugs in the global coordinate system. All local drawings should be mapped into a global coordinate system shared by all devices. To render the drawings on the screen, the device should convert these global coordinates to local coordinates. Local coordinates can then be used to render each drawable object. 
* Resume drawing after erasing
* Can't change color to black
* With 3 devices, only one device gets updates from both peers.
* Add pinch/zoom ability
* Progress is erased after rotating screen, app crashes
* Selecting Load Doc and then closing or going back from the popup screen crashes the app
* Recenter button
* Lightly colored outline around the canvas box
* Set default scale
