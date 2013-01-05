stb-remote
==========

The goal here is to control the Android odroidx board with others Android devices, that act like remote control.
This git repository contains two Android project (client side & server side)

It works like this :
- The remote client is the app installed in multiple android device, with a GUI with buttons that send commands through the WiFi to the remote server
- The remote server is the app installed in the odroidx board, it receive commands through the WIFi from the remote client. It is an android service that listen from multiple client.
- Every others app installed on the odroidx board, that want to use the remote control, have to bind to the remote server service, to throw KeyEvent.

There is two android apk to deploy :
- remote_client.apk on multiple android phone, to control the STB via a remote control interface.
- remote_server.apk on the STB, that catch android commands from the apk deployed on the android user devices.
