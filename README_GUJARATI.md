# MAX Assistant — Starter ZIP

આ Android પ્રોજેક્ટ શરૂઆતનો નમૂનો છે; આ સંપૂર્ણ Jarvis-સ્તરનું ફોન કંટ્રોલ નથી અને APK બિલ્ડ/ફોન પર પરીક્ષણ કર્યા વગર કોઈ પણ ભૂલ નહીં આવે તેની ખાતરી આપી શકાય નહીં.

## હાલના starterમાં
- MAX નામ સાથે હળવા રંગનું સરળ Home UI
- Hindi speech recognition અને ફોનમાં ઉપલબ્ધ Hindi Text-to-Speech
- YouTube, Play Store અને WhatsApp ખોલવાના commands
- YouTube search માટે browser fallback
- Flashlight on/off
- Accessibility service ચાલુ હોય તો scroll forward કરવાનો પ્રયાસ
- Charging connected/disconnected પર બોલવાની શરૂઆતની રચના
- GitHub Actionsથી debug APK બનાવવાની workflow

## મર્યાદાઓ
- Screenshotનું UI આ starterમાં અંદાજિત છે; screenshot સાથે pixel-perfect નથી.
- “Hello MAX” હંમેશા backgroundમાં સાંભળવું હાલ બનાવેલું નથી. Androidમાં background microphone માટે સ્પષ્ટ પરવાનગી/foreground service અને battery restrictions સંભાળવા પડે.
- Airplane Mode, Wi-Fi, Bluetooth અને Mobile Data પર Android વર્ઝન/ફોન ઉત્પાદકની મર્યાદા છે. આ starter Quick Settings/Internet panel ખોલે છે; બધા switches પોતે ફેરવતો નથી.
- WhatsApp પર contact પસંદ કરીને મેસેજ મોકલવું/auto-reply હજી implement કરેલું નથી. તેના માટે user-confirmed flow, notification access અને privacy controls જરૂરી છે.
- Play Store એપ શોધી/ખોલી શકે છે; ઇન્સ્ટોલ કરવા માટે Play Storeમાં user action જરૂરી હોઈ શકે.
- Indian female voice ફોનમાં ઇન્સ્ટોલ થયેલા TTS voices પર આધારિત છે. Settings → Text-to-speech output માં Hindi voice પસંદ/ડાઉનલોડ કરો.
- Accessibility permission ફક્ત તમે સમજ્યા પછી જ આપો; તે સ્ક્રીન પરની માહિતી વાંચી શકે છે.

## GitHub પરથી APK બનાવવાની રીત (ફોનથી)
1. ZIP extract કરો.
2. GitHubમાં નવી repository બનાવો, ઉદાહરણ `MAX-Assistant`.
3. ZIPના અંદરના files/folders repositoryના rootમાં upload કરો. `.github/workflows/android.yml` પણ જવું જોઈએ.
4. `Actions` tab ખોલો. જો પૂછે તો workflows enable કરો.
5. `Build MAX Android APK` workflow પસંદ કરીને `Run workflow` દબાવો.
6. Build સફળ થાય તો workflow run ખોલો → Artifacts → `MAX-debug-apk` download કરો.
7. ZIP extract કરીને `app-debug.apk` ઇન્સ્ટોલ કરો. Android પૂછે તો ફક્ત આ જાણીતી ફાઇલ માટે install permission આપો.

## Termux
આ projectને માત્ર Termuxથી build કરવું સહેલું/વિશ્વસનીય નથી કારણ કે Android SDK અને build tools જોઈએ. ફોનથી build કરવા માટે ઉપરની GitHub Actions રીત વાપરો. Termuxમાં સામાન્ય shell commandsથી APK build થઈ જશે એવું માનશો નહીં.

## પહેલાં શું સેટ કરવું
- MAX ખોલો અને Microphone permission આપો.
- સ્ક્રોલ કમાન્ડ માટે Settings → Accessibility → MAX Assistant ચાલુ કરો.
- Hindi TTS voice ન હોય તો ફોનની Text-to-speech settingsમાં Hindi voice ડાઉનલોડ કરો.
- ટોર્ચ માટે permission પૂછાય તો મંજૂરી આપો.
