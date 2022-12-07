# callable-spring-boot
URL for Rest API test https://jsonplaceholder.typicode.com/todos
# Create Bean for RestTemplate
	
	@SpringBootApplication
	public class CallableFutureApplication {

			public static void main(String[] args) {
					SpringApplication.run(CallableFutureApplication.class, args);
			}

			@Bean
			public RestTemplate getRestTemplate(){
					return new RestTemplate();
			}

	}

# Controller first example:

	@RestController
	public class TestController {

			@Autowired
			RestTemplate restTemplate;

			@GetMapping("/mytest")
			public String myTest(){
					long start = System.currentTimeMillis()/1000;
					for(int i = 0; i < 100; i++){
							restTemplate.getForObject("https://jsonplaceholder.typicode.com/todos", String.class);
					}
					long end = System.currentTimeMillis()/1000;
					return "success in : " + (end - start) + " s";
			}
		}
	
# Result

![Capture d’écran 2022-12-07 132801](https://user-images.githubusercontent.com/39261811/206180799-8e907db7-8c74-4822-94d5-6cb938f7673b.jpg)

# With Callable 

	# Service for implementing Callable

	@Component
	public class MyService implements Callable<String> {

			@Autowired
			public RestTemplate restTemplate;

			@Override
			public String call() throws Exception {
					return restTemplate.getForObject("https://jsonplaceholder.typicode.com/todos", String.class);
			}
	}
in the Controller	

	@RestController
	public class TestController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MyService service;

    @GetMapping("/mytest")
    public String myTest(){
        long start = System.currentTimeMillis()/1000;
        for(int i = 0; i < 100; i++){
            restTemplate.getForObject("https://jsonplaceholder.typicode.com/todos", String.class);
        }
        long end = System.currentTimeMillis()/1000;
        return "success in : " + (end - start) + " s";
    }

    @GetMapping("/myTest")
    public String myCallable() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis()/1000;

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<String>> futureList = new ArrayList<>();

        for(int i = 0; i < 100; i++){
            Future<String> future = executorService.submit(service);
            futureList.add(future);
        }
        for(Future<String> future: futureList){
            future.get();
        }
        long end = System.currentTimeMillis()/1000;
        return "success in : " + (end - start) + " s";
    }
	
# Result: 

![Capture d’écran 2022-12-07 132906](https://user-images.githubusercontent.com/39261811/206181484-8971681e-6495-44c2-bda0-d3228f570542.jpg)

