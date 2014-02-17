/*
 * BPF program compilation tool
 *
 * Generates decimal output, similar to `tcpdump -ddd ...`.
 * Unlike tcpdump, will generate for any given link layer type.
 *
 * Written by Willem de Bruijn (willemb@google.com)
 * Copyright Google, Inc. 2013
 * Licensed under the GNU General Public License version 2 (GPLv2)
*/

#include <pcap.h>
#include <stdio.h>

int main(int argc, char **argv)
{
	struct bpf_program program;
	struct bpf_insn *ins;
	int i, dlt = DLT_RAW;

	if (argc < 2 || argc > 3) {
		fprintf(stderr, "Usage:    %s [link] '<program>'\n\n"
				"          link is a pcap linklayer type:\n"
				"          one of EN10MB, RAW, SLIP, ...\n\n"
				"Examples: %s RAW 'tcp and greater 100'\n"
				"          %s EN10MB 'ip proto 47'\n'",
				argv[0], argv[0], argv[0]);
		return 1;
	}

	if (argc == 3) {
		dlt = pcap_datalink_name_to_val(argv[1]);
		if (dlt == -1) {
			fprintf(stderr, "Unknown datalinktype: %s\n", argv[1]);
			return 1;
		}
	}

	if (pcap_compile_nopcap(65535, dlt, &program, argv[argc - 1], 1,
				PCAP_NETMASK_UNKNOWN)) {
		fprintf(stderr, "Compilation error\n");
		return 1;
	}

	//printf("%d,\n", program.bf_len);
	ins = program.bf_insns;
	printf("// %s : %s\n",argv[argc-2],argv[argc-1]);
	printf("byte[] bpf_bytes={\n");
	printf("  //size opcode:16  jt:8  jf:8  k:32  (8 bytes)\n");
	for (i = 0; i < program.bf_len-1; ++ins, ++i) {
		printf("  //%04d : 0x%x 0x%x 0x%x 0x%x\n",
		        i+1,
		        ins->code,
	                ins->jt, ins->jf, 
	                ins->k
		      );
		printf("  (byte)0x%02x,(byte)0x%02x, (byte)0x%02x,(byte)0x%02x, (byte)0x%02x,(byte)0x%02x,(byte)0x%02x,(byte)0x%02x,\n",
		        ins->code>>8, ins->code&0xff,
	                ins->jt, ins->jf, 
	                ins->k >>24 & 0xff,
	                ins->k >>16 & 0xff,
	                ins->k >>8 & 0xff,
	                ins->k & 0xff
		      );
	}
	//last line
	printf("  //%04d : 0x%x 0x%x 0x%x 0x%x\n",
	        i+1,
	        ins->code,
                ins->jt, ins->jf, 
                ins->k
	      );
	printf("  (byte)0x%02x,(byte)0x%02x, (byte)0x%02x,(byte)0x%02x, (byte)0x%02x,(byte)0x%02x,(byte)0x%02x,(byte)0x%02x\n",
		        ins->code>>8, ins->code&0xff,
	                ins->jt, ins->jf, 
	                ins->k >>24 & 0xff,
	                ins->k >>16 & 0xff,
	                ins->k >>8 & 0xff,
	                ins->k & 0xff
		      );
	printf("};\n");
	pcap_freecode(&program);
	return 0;
}
