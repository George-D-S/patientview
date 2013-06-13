﻿
Partial Class study
    Inherits System.Web.UI.Page

    Protected Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load


        Dim lnkAbout As HyperLink = Master.FindControl("lnkAbout")
        Dim lnkStudy As HyperLink = Master.FindControl("lnkStudy")
        Dim lnkProfessionals As HyperLink = Master.FindControl("lnkProfessionals")
        Dim lnkPatients As HyperLink = Master.FindControl("lnkPatients")
        Dim lnkDiseases As HyperLink = Master.FindControl("lnkDiseases")

        Dim lblSpace As Label = Master.FindControl("lblSpace")
        Dim lblSpace2 As Label = Master.FindControl("lblSpace2")
        Dim lblSpace3 As Label = Master.FindControl("lblSpace3")
        Dim lblSpace4 As Label = Master.FindControl("lblSpace4")


        Dim intD As Integer = Request.QueryString("d")

        Select Case intD



            Case 1

                lblTitle.Text = "SRNS Study Group"

            Case 2

                lblTitle.Text = "MPGN Study Group"

            Case 3

                lblTitle.Text = "Dense Deposit Disease Study Group"

            Case Else

        End Select

        Select Case intD

            Case 1

                lnkAbout.Text = "About SRNS"
                lnkAbout.NavigateUrl = "about.aspx?d=1"
                lnkAbout.Visible = True
                lnkStudy.Text = "SRNS Study Group"
                lnkStudy.NavigateUrl = "study.aspx?d=1"
                lnkStudy.Visible = True
                lnkProfessionals.Visible = True
                lnkProfessionals.NavigateUrl = "professionals.aspx?d=1"
                lnkPatients.Visible = True
                lnkPatients.NavigateUrl = "patients.aspx?d=1"
                lblSpace.Visible = True
                lblSpace2.Visible = True
                lblSpace3.Visible = True
                lblSpace4.Visible = True
                lnkDiseases.Visible = True

            Case 2

                lnkAbout.Text = "About MPGN"
                lnkAbout.NavigateUrl = "about.aspx?d=2"
                lnkAbout.Visible = True
                lnkStudy.Text = "MPGN Study Group"
                lnkStudy.Visible = True
                lnkStudy.NavigateUrl = "study.aspx?d=2"
                lnkProfessionals.Visible = True
                lnkProfessionals.NavigateUrl = "professionals.aspx?d=2"
                lnkPatients.NavigateUrl = "patients.aspx?d=2"
                lnkPatients.Visible = True
                lblSpace.Visible = True
                lblSpace2.Visible = True
                lblSpace3.Visible = True
                lblSpace4.Visible = True
                lnkDiseases.Visible = True

            Case 3

                lnkAbout.Text = "About DDD"
                lnkAbout.NavigateUrl = "about.aspx?d=3"
                lnkAbout.Visible = True
                lnkStudy.Text = "DDD Study Group"
                lnkStudy.NavigateUrl = "study.aspx?d=3"
                lnkStudy.Visible = True
                lnkProfessionals.Visible = True
                lnkProfessionals.NavigateUrl = "professionals.aspx?d=3"
                lnkPatients.NavigateUrl = "patients.aspx?d=3"
                lnkPatients.Visible = True
                lblSpace.Visible = True
                lblSpace2.Visible = True
                lblSpace3.Visible = True
                lblSpace4.Visible = True
                lnkDiseases.Visible = True


            Case Else

                lnkAbout.Visible = False
                lnkStudy.Visible = False
                lnkProfessionals.Visible = False
                lnkPatients.Visible = False
                lnkDiseases.Visible = False

                lblSpace.Visible = False
                lblSpace2.Visible = False
                lblSpace3.Visible = False
                lblSpace4.Visible = False


        End Select


    End Sub
End Class
