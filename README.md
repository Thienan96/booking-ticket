Demo project resolve problem concurrency when booking tikcet at high demand time
1. Configuration redis
2. Congiguration kafka
3. Ensure number of ticket are sold are correct with available amount of ticket and people come first buy first
4. Specific number of people is waiting
5. Protect server when too much request at the same time: vegeta attack -targets=targets.txt -rate=2000 -duration=2s | tee results.bin