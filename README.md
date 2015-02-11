# HackU 3 ODU Team
## Getting Started.
<a href="" target="_blank"></a>
- <a href="https://github.com/YOUR_USERNAME/hacku3/fork" target="_blank">Fork this repo.</a>
- Clone the fork to your local environment. `git clone https://github.com/YOUR_USERNAME/hacku3.git`

### LAMP Environment
The suggested way to run the server is to use <a href="https://vagrantup.com" target="_blank">Vagrant</a>.

While you could just run this code on your current LAMP set up, Vagrant gives you all the dependencies out of the box and ensures that everyone is using the same environment.

The following steps are for setting up the server using Vagrant.

- Install <a href="https://vagrantup.com" target="_blank">Vagrant</a> and <a href="https://virtualbox.org" target="_blank">VirtualBox</a>
- Navigate to the web folder under the HackU3 directory.
- Run the command `vagrant up` to start the virtual machine.
- Once the process finishes, enter `vagrant ssh` to ssh into the virtual machine.
- Enter `cd /vagrant` to switch the the working directory.
- Run `grunt server` to start the development server.
- On your host machine, point your browser to <a href="http://localhost:8080" target="_blank">http://localhost:8080</a> and you should see a welcome page.

Any changes you make to the files under hacku3/web, will be synced with the virtual machine and your browser will refresh automatically.

To lear more about vagrant, checkout their <a href="http://docs.vagrantup.com/v2/why-vagrant/index.html" target="_blank">documentation</a>

### Android App
Open the Android App folder using Android Studio. Android Studio will prompt you to install any missing dependencies. Try to compile and run the app on an emulator.
