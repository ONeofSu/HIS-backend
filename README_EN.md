# **HIS - Biomedical Information System (Backend)**  
*A microservices-based backend for Traditional Chinese Medicine (TCM) digital management*  

## **1. Project Overview**  
**Frontend-Backend Separation Architecture**  
- This repository contains the **backend** (microservices).  
- Frontend repository: [https://github.com/software-engineer-group-oasis/chinese-medicine.git](https://github.com/software-engineer-group-oasis/chinese-medicine.git)  

**System Features**  
A comprehensive digital platform integrating:  
- TCM resource collection & data aggregation  
- Interactive visualization  
- Research/teaching resource management  
- Quality evaluation & performance tracking  
- **AI-enhanced TCM Q&A** (fine-tuned LLM for professional responses)  
- **Real-time live streaming** for educational purposes  

**Target Users**: Research institutions, medical universities, and pharmaceutical enterprises.  

---

## **2. System Modules**  
| Module | Key Functions |
|--------|--------------|
| **1. Medicinal Material Distribution** | Interactive maps (heatmaps/clustering), regional filters, detail pages |
| **2. Data Collection & Exchange** | Mobile data submission (GPS/images), audit trails, metadata tracking |
| **3. Course Management** | Structured courses (videos/PDFs/labs), tagging, TCM encyclopedia links |
| **4. Research Project Management** | Project lifecycle tracking, document uploads, cross-module associations |
| **5. Training Materials** | Short-term workshops (videos/lectures), access analytics, skill-focused content |
| **6. TCM Quality Evaluation** | Custom evaluation forms, versioned standards, exportable reports |
| **7. Performance Management** | Workload tracking, automated approval workflows, multi-format evidence |

---

## **3. Tech Stack**  
| Category | Technologies |
|----------|--------------|
| **Backend** | SpringBoot 3.x, SpringMVC, MyBatis-Plus |
| **Database** | MySQL 8.0 |
| **Caching/MQ** | Redis, RabbitMQ |
| **AI Integration** | Python 3.12 + Fine-tuned LLM (NVIDIA RTX 4060, 8GB VRAM) |
| **Live Streaming** | SRS Server (Docker) |
| **Dev Environment** | Windows 11, JDK 21 |

---

## **4. Key Features**  
### **4.1 Information Portal**  
- Login/registration (email/password)  
- **5-tier RBAC**: Public user → Student → Teacher → Admin → Super Admin  

### **4.2 Medicinal Material Distribution**  
- Chongqing-focused heatmaps with drill-down by district  
- Detail pages with GPS coordinates, uploader info, and encyclopedia links  

### **4.3 Mobile Data Collection**  
- Field uploads with auto-GPS tagging and manual review  

### **4.4 AI Integration**  
- Launch via `model_server.py` (Python 3.12)  
- Configure `AIGenerateService.java` with endpoint  

### **4.5 Live Streaming**  
- OBS-powered teacher broadcasts (SRS server required)  

---

## **5. API Endpoints**  
- Authentication  
- Material Distribution & Encyclopedia  
- Data Collection  
- Course/Research/Training Management  
- Performance Tracking  
- Comment System  

---

## **6. Contribution Guide**  
1. Fork this repository  
2. Create a feature branch: `git checkout -b feature/your-feature`  
3. Commit changes: `git commit -m 'Add feature'`  
4. Push: `git push origin feature/your-feature`  
5. Open a **Pull Request**  

---

## **7. Deployment Notes**  
```bash
# For AI server (Python 3.12 + GPU required)
python HIS-Python/model_server.py

# For live streaming (Docker)
docker pull ossrs/srs:4
```