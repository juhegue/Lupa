;--------------------------------
;Include Modern UI

    !include "MUI2.nsh"
    !include "x64.nsh"

;-------------------------------
; Iconos del instalador

    !define MUI_ICON "lupa.ico"
    !define MUI_HEADERIMAGE
    !define MUI_HEADERIMAGE_BITMAP "lupa.bmp"
    !define MUI_HEADERIMAGE_RIGHT

;--------------------------------
;General
    !define APP "lupa"
    !define DES "Lupa"
    !define EMP "Juhegue"

    ;Name and file
    Name "${DES}"
    OutFile "${APP}_install.exe"

    ;Default installation folder.  No se usa ya que enla funcion .onInit se asigna
;   InstallDir "$PROGRAMFILES64\${APP}"

    ;Get installation folder from registry if available
    InstallDirRegKey HKCU "Software\${EMP}\${APP}" ""

    ;Request application privileges for Windows Vista
    RequestExecutionLevel admin

;--------------------------------
;Obtiene el path de instalacion

    Function .onInit
        ${If} $InstDir == ""
            ${If} ${RunningX64}
                StrCpy $InstDir "$ProgramFiles64\${EMP}\${APP}"
            ${Else}
                StrCpy $InstDir "$ProgramFiles32\${EMP}\${APP}"
            ${EndIf}
        ${EndIf}
    FunctionEnd

;--------------------------------
;Interface Settings

    !define MUI_ABORTWARNING

;--------------------------------
;Pages

    !insertmacro MUI_PAGE_DIRECTORY
    !insertmacro MUI_PAGE_INSTFILES

    !insertmacro MUI_UNPAGE_CONFIRM
    !insertmacro MUI_UNPAGE_INSTFILES

;--------------------------------
;Languages

    !insertmacro MUI_LANGUAGE "Spanish"

;--------------------------------
;Installer Sections

Section "JRE" SEC01
    Call DetectJRE
SectionEnd


;Functions
Function DetectJRE
    StrCpy $1 "SOFTWARE\JavaSoft\Java Runtime Environment"
    StrCpy $2 0
    ReadRegStr $2 HKLM "$1" "CurrentVersion"
    StrCmp $2 "" DetectTry2
    ReadRegStr $5 HKLM "$1\$2" "JavaHome"
    StrCmp $5 "" DetectTry2
    goto done

    DetectTry2:
        ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
        StrCmp $2 "" NoJava
        ReadRegStr $5 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$2" "JavaHome"
        StrCmp $5 "" NoJava done

    done:
        ;MessageBox MB_OK "$2 Java instalado"
        return
    NoJava:
        MessageBox MB_OK "Java no instalado. Debe instalar java para poder ejecutar el programa."
        return
 FunctionEnd

Section "Dummy Section" SecDummy

    SetOutPath "$INSTDIR"

	File "lupa.jar"
	File "lupa.ico"

    CreateShortCut "$DESKTOP\${APP}.lnk" "$INSTDIR\${APP}.jar" "" "$INSTDIR\${APP}.ico"

    CreateDirectory "$SMPROGRAMS\${EMP}"
    CreateDirectory "$SMPROGRAMS\${EMP}\${APP}"
    CreateShortCut "$SMPROGRAMS\${EMP}\${APP}\${APP}.lnk" "$INSTDIR\${APP}.jar" "" "$INSTDIR\${APP}.ico"
    CreateShortCut "$SMPROGRAMS\${EMP}\${APP}\Uninstall.lnk" "$INSTDIR\Uninstall.exe" "" "$INSTDIR\Uninstall.exe" 0

    ;Store installation folder
    WriteRegStr HKCU "Software\${EMP}\${APP}" "" $INSTDIR

    ;Create uninstaller
    WriteUninstaller "$INSTDIR\Uninstall.exe"

SectionEnd


;--------------------------------
;Uninstaller Section

Section "Uninstall"

    Delete "$INSTDIR\lupa.jar"
    Delete "$INSTDIR\lupa.ico"
    Delete "$INSTDIR\lupa.cfg"

    RMDir "$INSTDIR"

    DeleteRegKey /ifempty HKCU "Software\${EMP}\${APP}"

    Delete "$DESKTOP\${APP}.lnk"

    Delete "$SMPROGRAMS\${EMP}\${APP}\${APP}.lnk"
    Delete "$SMPROGRAMS\${EMP}\${APP}\Uninstall.lnk"
    RMDIR  "$SMPROGRAMS\${EMP}\${APP}"
    RMDIR  "$SMPROGRAMS\${EMP}"

    Delete "$INSTDIR\Uninstall.exe"

SectionEnd