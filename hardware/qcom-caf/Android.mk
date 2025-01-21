QCOM_CAF_ROOT_PATH := $(call my-dir)

# Board platforms lists to be used for
# TARGET_BOARD_PLATFORM specific featurization
QCOM_BOARD_PLATFORMS += msm8952 msm8996 msm8998 sdm660 sdm845 sm6125 sm8150 sm8250

# Some supported platforms need a different media hal
# This list selects platforms that should use the latest media hal
# All other platforms automatically fallback to the legacy hal
QCOM_NEW_MEDIA_PLATFORM := sdm845 sm6125 sm8150 sm8250

#List of targets that use video hw
MSM_VIDC_TARGET_LIST := msm8952 msm8996 msm8998 sdm660 sdm845 sm6125 sm8150 sm8250

#List of targets that use master side content protection
MASTER_SIDE_CP_TARGET_LIST := msm8996 msm8998 sdm660 sdm845 sm6125 sm8150 sm8250

TARGET_KERNEL_VERSION := 4.14

include $(QCOM_CAF_ROOT_PATH)/utils.mk

include $(call all-subdir-makefiles)
