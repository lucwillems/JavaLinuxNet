#!/bin/bash
# run this script to setup lowlevel linux environment for testing
#
if [ -z "$SUDO_USER" ];then
   echo "Please use sudo $0"
   exit 0
fi

#get user & primary group
TUN_USER=$SUDO_USER
TUN_GROUP=`groups $SUDO_USER | awk '{print $3}'`

# Note we abuse 8.8.4.4 as test IP
TESTIP=8.8.4.4
TESTNET=8.8.4.0/24
TUNDEV=test
USER=$TUN_USER
GROUP=$TUN_GROUP
TUNDEV2=testnotused
USER=$TUN_USER
GROUP=$TUN_GROUP

ip tuntap del dev $TUNDEV mode tun
ip tuntap add dev $TUNDEV mode tun user $USER group $GROUP
ip addr add 127.0.0.2/32 dev $TUNDEV
ip link set dev $TUNDEV up
ip route add $TESTNET dev $TUNDEV
echo "1" > /proc/sys/net/ipv4/conf/$TUNDEV/log_martians
echo "0" > /proc/sys/net/ipv4/conf/$TUNDEV/rp_filter


ip tuntap del dev $TUNDEV2 mode tun
ip tuntap add dev $TUNDEV2 mode tun user $USER group $GROUP
ip addr add 127.0.0.3/32 dev $TUNDEV2
ip link set dev $TUNDEV2 up

#route all redirected traffic to lo
ip rule del priority 2000
ip rule add priority 2000 fwmark 1 lookup 100
ip route flush table 100
ip -f inet route add local 0.0.0.0/0 dev lo table 100

iptables -F
iptables -t mangle -F
iptables -t mangle -N DIVERT
iptables -t mangle -A DIVERT -j MARK --set-mark 1
iptables -t mangle -A DIVERT -j ACCEPT

#for forwarding traffic
iptables -t mangle -A PREROUTING -p tcp -m socket -j DIVERT
iptables -t mangle -A PREROUTING -p tcp -m tcp --destination $TESTIP --dport 80 -j TPROXY --on-port 1800 --on-ip 0.0.0.0 --tproxy-mark 0x01/0x01

#for local traffic
iptables -t mangle -A OUTPUT -p tcp -m tcp --destination $TESTIP --dport 80 -j MARK --set-mark 0x01

#get some packet loss and delay
#tc qdisc del dev lo root netem
#tc qdisc add dev lo root netem loss 1% delay 100ms 20ms

