# 🚀 Strimzi Multi-Environment Kafka: The Ultimate Cost Optimization Solution

## 🔥 The Problem (Or: The Money Pit You're Currently Digging)

Picture this: It's 2025, and you're running an event-driven architecture. Beautiful, scalable, modern... and **EXPENSIVE AS HELL**. 

You've got separate Kafka clusters for:
- 🔨 Development
- 🧪 QA
- 🎭 Staging  
- 🎯 Prod or any other

Each cluster costs you hundreds per month. AWS MSK? Azure Event Hubs? Confluent Cloud? They're all laughing their way to the bank while your CFO is crying in meetings about infrastructure costs.

**Let's do some math that'll make you cry:**

| Setup | Monthly Cost (Conservative) | Annual Cost |
|-------|---------------------------|-------------|
| **Traditional: 3 Separate Clusters** | | |
| • Dev Cluster (3 brokers) | ~$300 | ~$3,600 |
| • QA Cluster (3 brokers) | ~$300 | ~$3,600 |
| • Staging Cluster (3 brokers) | ~$300 | ~$3,600 |
| **TOTAL** | **~$900/mo** | **~$10,800/year** |
| | | |
| **This Solution: 1 Unified Cluster** | | |
| • Single Cluster (3 brokers) | ~$300 | ~$3,600 |
| **TOTAL** | **~$300/mo** | **~$3,600/year** |
| | | |
| **💰 YOU SAVE** | **~$600/mo** | **~$7,200/year** |

And that's just for 3 environments with modest sizing! Scale up to production-grade clusters and you're looking at **$15k+/year in savings**.

---

## 🦸 Enter: The Saviour of the Saviours

Behold! The ruler of optimization and isolation! The well in the middle of the desert! The cool breeze of wind on the hottest day on Earth! The hero your budget deserves AND needs right now!

**Introducing: Strimzi-Operated Single Cluster Kafka with Environment Isolation** 🎉

This is not just another Kafka setup. This is the **Marie Kondo** of event-driven architecture - it sparks joy (in your finance team) by ruthlessly optimizing what you don't need while keeping everything that you do.

### 🎯 What Makes This Magical?

✨ **One Cluster, Multiple Environments** - Dev, QA, Staging all living harmoniously in the same cluster  
🔒 **Topic-Level Isolation** - Each environment gets its own topic prefix (`dev.*`, `qa.*`, `staging.*`)  
🛡️ **User-Level Authentication** - SCRAM-SHA-512 authentication( you can use other types ) ensuring only authorized users access their environments  
🎪 **ACL-Based Authorization** - Granular permissions so dev-user can't accidentally nuke other environment topics  
📊 **Strimzi Operator Magic** - Kubernetes-native, declarative, GitOps-ready  
⚡ **KRaft Mode** - No ZooKeeper needed (because it's 2025, guys!)  
💪 **Scalable Architecture** - 3 broker/controller nodes with room to grow

---

## 🏗️ Architecture Specs

### Cluster Configuration

**Kafka Version:** 4.1.1 (Latest KRaft-enabled)

**Node Pools:**
- **3 Broker/Controller Nodes** - Combined roles for optimal resource utilization
- **Ephemeral Storage** - Perfect for dev/test environments (use persistent storage for production)
- **Replication Factor: 3** - High availability across all nodes

**Listeners:**
- **Internal Listener:** Port 9092 (SASL_PLAINTEXT with SCRAM-SHA-512 or any other)

**Authorization:**
- **Type:** Simple ACL-based authorization
- **Authentication:** SCRAM-SHA-512 for all users

### Environment Users

| User | Topic Prefix | Consumer Group Prefix | Permissions |
|------|-------------|----------------------|-------------|
| `dev-user` | `dev.*` | `dev.*` | Read, Write, Create, Describe |
| `qa-user` | `qa.*` | `qa.*` | Read, Write, Create, Describe |
| `staging-user` | `staging.*` | `staging.*` | Read, Write, Create, Describe |

Each user is **completely isolated** from other environments. No accidental cross-contamination!

### How It Works

```
┌─────────────────────────────────────────────────┐
│         Single Kafka Cluster (3 Brokers)        │
├─────────────────────────────────────────────────┤
│                                                 │
│  ┌──────────┐  ┌──────────┐  ┌──────────────┐  │
│  │ dev-user │  │ qa-user  │  │ staging-user │  │
│  └────┬─────┘  └────┬─────┘  └──────┬───────┘  │
│       │             │                │          │
│       ↓             ↓                ↓          │
│  ┌────────┐   ┌────────┐     ┌──────────┐     │
│  │dev.xyz │   │qa.test │     │staging.* │     │
│  │dev.abc │   │qa.data │     │staging.* │     │
│  └────────┘   └────────┘     └──────────┘     │
│                                                 │
│  Authentication: SCRAM-SHA-512                  │
│  Authorization: ACL-based per prefix            │
└─────────────────────────────────────────────────┘
```

---

## 📋 Prerequisites

Before you embark on this cost-saving journey, you'll need:

- ☸️ **Minikube** (or any Kubernetes cluster)
  - Minimum: 8GB RAM, 4 CPUs
  - Multi-node setup recommended (1 control plane + 2 workers)
- 🐳 **Docker** (for building Spring Boot apps)
- 🔧 **kubectl** (to talk to your cluster)
- ☕ **Java 17+** (if building locally)
- 📦 **Maven** (optional, Dockerfile has it built-in)
- 🎯 **Strimzi Operator** (we'll install this)

---

## 🚀 Installation & Setup

### Step 1: Install Strimzi Operator

```bash
# Create kafka namespace
kubectl create namespace kafka

# Install Strimzi operator
kubectl create -f 'https://strimzi.io/install/latest?namespace=kafka' -n kafka

# Wait for operator to be ready
kubectl wait --for=condition=ready pod -l name=strimzi-cluster-operator -n kafka --timeout=300s
```

### Step 2: Deploy Kafka Cluster

```bash
# Apply Kafka cluster configuration
kubectl apply -f kafka-kraft.yaml

# Apply node pool (3 broker/controller nodes)
kubectl apply -f kafka-node-pool.yaml

# Wait for Kafka to be ready (this takes 2-3 minutes)
kubectl wait kafka/my-kafka --for=condition=Ready --timeout=300s -n kafka
```

### Step 3: Create Environment Users

```bash
# Create dev, qa, and staging users with ACLs
kubectl apply -f Kafka-users-with-acl.yaml

# Verify users are created
kubectl get kafkausers -n kafka
```

### Step 4: Extract User Credentials

```bash
# Get dev-user password
kubectl get secret dev-user -n kafka -o jsonpath='{.data.password}' | base64 -d
echo

# Get qa-user password
kubectl get secret qa-user -n kafka -o jsonpath='{.data.password}' | base64 -d
echo

# Get staging-user password
kubectl get secret staging-user -n kafka -o jsonpath='{.data.password}' | base64 -d
echo
```

**Important:** Save these passwords! You'll need them for your applications.

---

## 🧪 Testing with Spring Boot Apps

### Producer Application (Dev Environment)

#### Build & Deploy

```bash
cd kafka-producer

#Edit this with the respective credentials you obtained
cd src/main/resources/
replace spring.kafka.properties.(saal.jaas.config) with your obtained credentials

# Build Docker image
docker build -t kafka-producer:1.0.0 .

# Load into Minikube
minikube image load kafka-producer:1.0.0

# Deploy to Kubernetes
kubectl apply -f producer-deployment.yaml

# Wait for pod to be ready
kubectl wait --for=condition=ready pod -l app=kafka-producer -n kafka --timeout=120s
```

#### Test the Producer

```bash
# Port-forward the service
kubectl port-forward -n kafka svc/kafka-producer-dev 8080:8080

# Send a test message
curl -X POST http://localhost:8080/api/messages/send \
  -H "Content-Type: text/plain" \
  -d "Hello from dev environment!"

# Send with a key
curl -X POST "http://localhost:8080/api/messages/send?key=order-123" \
  -H "Content-Type: text/plain" \
  -d '{"orderId": "123", "amount": 99.99}'

# Check health
curl http://localhost:8080/api/messages/health
```

### Consumer Application (Dev Environment)

#### Build & Deploy

```bash
cd kafka-consumer

#Edit this with the respective credentials you obtained
cd src/main/resources/
replace spring.kafka.properties.(saal.jaas.config) with your obtained credentials

# Build Docker image
docker build -t kafka-consumer:1.0.0 .

# Load into Minikube
minikube image load kafka-consumer:1.0.0

# Deploy to Kubernetes
kubectl apply -f consumer-deployment.yaml

# Wait for pod to be ready
kubectl wait --for=condition=ready pod -l app=kafka-consumer -n kafka --timeout=120s
```

#### Watch Consumer Logs

```bash
# See messages being consumed in real-time
kubectl logs -n kafka -l app=kafka-consumer -f
```

#### Query Consumed Messages

```bash
# Port-forward the service
kubectl port-forward -n kafka svc/kafka-consumer-dev 8081:8081

# Get all consumed messages
curl http://localhost:8081/api/messages/consumed

# Get message count
curl http://localhost:8081/api/messages/count

# Check health
curl http://localhost:8081/api/messages/health
```

---

## 🔍 Manual Testing (CLI)

Want to test directly from Kafka? Here's how:

```bash
# Exec into a broker pod
kubectl exec -it -n kafka my-kafka-kafka-brokers-0 -- bash

# Produce messages
bin/kafka-console-producer.sh \
  --bootstrap-server localhost:9092 \
  --topic dev.xyz \
  --producer-property security.protocol=SASL_PLAINTEXT \
  --producer-property sasl.mechanism=SCRAM-SHA-512 \
  --producer-property 'sasl.jaas.config=org.apache.kafka.common.security.scram.ScramLoginModule required username="dev-user" password="YOUR_PASSWORD";'

# Consume messages
bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic dev.xyz \
  --from-beginning \
  --group dev-consumer-group \
  --consumer-property security.protocol=SASL_PLAINTEXT \
  --consumer-property sasl.mechanism=SCRAM-SHA-512 \
  --consumer-property 'sasl.jaas.config=org.apache.kafka.common.security.scram.ScramLoginModule required username="dev-user" password="YOUR_PASSWORD";'
```

---

## 📊 Monitoring with ArgoCD (Optional)

Want to visualize your Kafka deployment? Deploy ArgoCD!

```bash
# Install ArgoCD
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

# Access ArgoCD UI
kubectl port-forward svc/argocd-server -n argocd 8080:443

# Get admin password
kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d
```

Create an ArgoCD application pointing to your Git repo and watch your Kafka cluster sync beautifully!

---

## 🎓 What You've Learned

By implementing this solution, you now have:

✅ A cost-optimized multi-environment Kafka setup  
✅ Strong isolation between environments using ACLs  
✅ Secure SCRAM-SHA-512 authentication  
✅ KRaft-based Kafka (~2026 😣)  
✅ GitOps-ready declarative configuration  
✅ Production-ready Spring Boot producer/consumer apps  
✅ Kubernetes-native deployment with Strimzi  

---

## 🚧 What's Next?

This is the **local testing version**. Coming soon:

- 🌐 **Production-Grade Setup** with persistent storage
- 📈 **Monitoring Stack** (Prometheus + Grafana dashboards)
- 🔐 **TLS Encryption** for external listeners
- 🌍 **Multi-Region Deployment** patterns
- 🎯 **Schema Registry** integration
- 🔄 **Kafka Connect** for data pipelines
- 📦 **Helm Charts** for easier deployment

Stay tuned! ⭐ this repo to get notified.

---

## 💬 Questions? Feedback? High-Fives?

I'd love to hear from you!

📧 **Email:** as610271@gmail.com

Found a bug? Have a feature request? Want to share your success story? Reach out!

---

## 💰 Feeling Generous?

Did this project save your company **$10k+/year** in infrastructure costs? 

Consider buying me a veg sandwich with cheese(my fav) (or a whole eatery shop) as a thank you! 🥪

**Donate via:** as610271@gmail.com

Your contributions help me create more awesome open-source projects like this! 🥹

Every dollar saved in your infrastructure costs could be a dollar invested in making more developers' lives easier. Let's spread the love! 💙

---

## 📜 License

MIT License - feel free to use this in your company, modify it, sell it, tattoo it on your arm, whatever makes you happy!

---

## 🙏 Credits

Built with:
- ☸️ [Strimzi](https://strimzi.io/) - The best Kafka on Kubernetes operator
- 🍃 [Spring Boot](https://spring.io/projects/spring-boot) - Java framework that doesn't make you want to quit programming
- ⚓ [Kubernetes](https://kubernetes.io/) - Container orchestration (aka organized chaos)
- 🎯 [Apache Kafka](https://kafka.apache.org/) - The event streaming platform that started it all

---

**Remember:** With great power comes great responsibility... and with this setup comes great cost savings! 💪

Happy streaming! 🎉