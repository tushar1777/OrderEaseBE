# OrderEase
### Tech Used: 
- ReactJs: Frontend
- Backend: SpringBoot
- Deployed: AWS
- DB: AWS RDS (My SQL)
- CI/CD: Jenkins
- Coverage: SonarQube
- Automation: Katalon Studio

# Use Cases:
1. **User:**
    - ID (Auto-generated)
    - Username
    - Password
    - Email
    - Phone Number
    - Address
    - Role (Customer or Restaurant Owner)
    - Registration Date
    - Last Login Date
2. **Restaurant:**
    - ID (Auto-generated)
    - Name
    - Description
    - Cuisine Type
    - Address
    - Contact Information
    - Opening Hours
    - Ratings
    - Image URL
    - Registration Date
3. **Menu Item:**
    - ID (Auto-generated)
    - Name
    - Description
    - Price
    - Category
    - Image URL
    - Availability Status
    - Restaurant (reference to Restaurant entity)
    - Creation Date
4. **Order:**
    - ID (Auto-generated)
    - Customer (reference to User entity)
    - Restaurant (reference to Restaurant entity)
    - Total Amount
    - Order Status
    - Timestamp
    - Delivery Address
    - Items (list of Order Items)
    - Payment (reference to Payment entity, if applicable)
5. **Order Item:**
    - ID (Auto-generated)
    - Menu Item (reference to Menu Item entity)
    - Quantity
    - Subtotal
    - Order (reference to Order entity)
6. **Payment:**
    - ID (Auto-generated)
    - Order (reference to Order entity)
    - Payment Method
    - Payment Status
    - Total Amount
    - Payment Timestamp
7. **Review/Rating:**
    - ID (Auto-generated)
    - Customer (reference to User entity)
    - Restaurant (reference to Restaurant entity)
    - Rating
    - Review Text
    - Timestamp
8. **Promotion/Coupon:**
    - ID (Auto-generated)
    - Code
    - Discount Amount
    - Validity Period
    - Terms and Conditions
9. **Notification:**
    - ID (Auto-generated)
    - Recipient (reference to User, Restaurant, or Delivery Executive entity)
    - Message
    - Timestamp
    - Read Status
10. **Category:**
    - ID (Auto-generated)
    - Name
11. **Address:**
    - ID (Auto-generated)
    - Street Address
    - City
    - State/Province
    - Postal Code
    - Country
    
12. contact information
    - email
    - mobile
    - twitter
    - instagram
